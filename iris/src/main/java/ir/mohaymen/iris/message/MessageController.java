package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.chat.ChatType;
import ir.mohaymen.iris.contact.Contact;
import ir.mohaymen.iris.contact.ContactService;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.media.MediaService;
import ir.mohaymen.iris.subscription.SubDto;
import ir.mohaymen.iris.subscription.SubscriptionService;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.utility.BaseController;
import ir.mohaymen.iris.utility.Nameable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController extends BaseController {

    private final MessageService messageService;
    private final ModelMapper modelMapper;
    private final ChatService chatService;
    private final MediaService mediaService;
    private final SubscriptionService subscriptionService;
    private final ContactService contactService;

    private final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @GetMapping("/get-messages/{chatId}/{floor}/{ceil}")
    public ResponseEntity<List<GetMessageDto>> getMessages(@PathVariable("chatId") Long chatId, @PathVariable("floor") Integer floor, @PathVariable("ceil") Integer ceil) {
        if (ceil - floor > 50)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        List<Message> messages = (List<Message>) messageService.getByChat(chatService.getById(chatId));
        if (messages.size() < ceil)
            ceil = (messages.size());
        if (floor < 0)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        List<GetMessageDto> getMessageDtoList = new ArrayList<>();
        for (Message message : messages.subList(messages.size() - ceil, messages.size() - floor)) {
            getMessageDtoList.add(mapMessageToGetMessageDto(message));
        }
        return new ResponseEntity<>(getMessageDtoList, HttpStatus.OK);
    }

    @GetMapping("/seen-users/{chatId}/{messageId}")
    public ResponseEntity<List<SubDto>> usersSeen(@PathVariable Long chatId, @PathVariable Long messageId) {
        Iterable<Contact> contacts = contactService.getContactByFirstUser(getUserByToken());
        if (chatService.getById(chatId).getChatType() == ChatType.CHANNEL)
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        List<SubDto> users = new ArrayList<>();
        messageService.usersSeen(messageId, chatId).forEach(s -> {
            SubDto subDto = new SubDto();
            Nameable nameable = subscriptionService.setName(contacts, s.getUser());
            subDto.setFirstName(nameable.getFirstName());
            subDto.setLastName(nameable.getLastName());
            subDto.setUserId(s.getUser().getUserId());
            users.add(subDto);
        });
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/seen-user-count/{chatId}/{messageId}")
    public ResponseEntity<Integer> userSeenCount(@PathVariable Long chatId, @PathVariable Long messageId) {
        return new ResponseEntity<>(messageService.usersSeen(messageId, chatId).size(), HttpStatus.OK);
    }

    @PostMapping("/send-message")
    public ResponseEntity<GetMessageDto> sendMessage(@RequestBody @Valid MessageDto messageDto) {
        Chat chat = chatService.getById(messageDto.getChatId());
        User user = getUserByToken();
        Message repliedMessage = messageService.getById(messageDto.getRepliedMessageId());

        if (!chatService.isInChat(chat, user))
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);

        Media media;
        if (messageDto.getFileContentType() == null && messageDto.getFileName() == null && messageDto.getFilePath() == null) {
            if (messageDto.getText() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            else media = null;
        } else {
            media = modelMapper.map(messageDto, Media.class);
            mediaService.createOrUpdate(media);
        }
        Message message = new Message();
        message.setRepliedMessage(repliedMessage);
        message.setText(messageDto.getText());
        message.setChat(chat);
        message.setSender(user);
        message.setMedia(media);
        message.setSendAt(Instant.now());
        return new ResponseEntity<>(mapMessageToGetMessageDto(message), HttpStatus.OK);
    }

    @PatchMapping("/edit-message")
    public ResponseEntity<?> editMessage(@RequestBody @Valid EditMessageDto editMessageDto) {
        var user = getUserByToken();
        Message message = messageService.getById(editMessageDto.getMessageId());
        if (!Objects.equals(message.getSender().getUserId(), user.getUserId())) {
            logger.info(MessageFormat.format("user with phoneNumber:{0} wants to edit message with id:{1}!", user.getPhoneNumber(), message.getMessageId()));
            return new ResponseEntity<>("Access violation", HttpStatus.FORBIDDEN);
        }
        message.setText(editMessageDto.getText());
        message.setEditedAt(Instant.now());
        return new ResponseEntity<>(mapMessageToGetMessageDto(message), HttpStatus.OK);
    }

    @PostMapping("/forward-message/{chatId}/{messageId}")
    public ResponseEntity<ForwardMessageDto> forwardMessage(@PathVariable Long chatId, @PathVariable Long messageId) {
        User user = getUserByToken();
        Message message = messageService.getById(messageId);

        Message newMessage = new Message();
        newMessage.setChat(chatService.getById(chatId));
        newMessage.setOriginMessage(message);
        newMessage.setSender(user);
        message.setText(message.getText());

        Media newMedia = modelMapper.map(message.getMedia(), Media.class);
        newMedia.setMediaId(null);
        mediaService.createOrUpdate(newMedia);
        message.setMedia(newMedia);

        return new ResponseEntity<>(mapMessageToForwardMessageDto(message), HttpStatus.OK);
    }

    @PostMapping("/forward-message/{chatId}")
    public ResponseEntity<List<ForwardMessageDto>> forwardMessage(@PathVariable Long chatId, @RequestBody @Valid List<Long> messageIds) {
        List<ForwardMessageDto> forwardMessageDtos = new ArrayList<>();
        for (Long messageId : messageIds) {
            ResponseEntity<ForwardMessageDto> forwardMessageDtoResponseEntity = forwardMessage(chatId, messageId);
            ForwardMessageDto forwardMessageDto = forwardMessageDtoResponseEntity.getBody();
            forwardMessageDtos.add(forwardMessageDto);
        }

        return new ResponseEntity<>(forwardMessageDtos, HttpStatus.OK);
    }

    private GetMessageDto mapMessageToGetMessageDto(Message message) {
        GetMessageDto getMessageDto = modelMapper.map(messageService.createOrUpdate(message), GetMessageDto.class);
        getMessageDto.setUserId(message.getSender().getUserId());
        getMessageDto.setRepliedMessageId(message.getRepliedMessage().getMessageId());
        return getMessageDto;
    }

    private ForwardMessageDto mapMessageToForwardMessageDto(Message message) {
        ForwardMessageDto forwardMessageDto = modelMapper.map(messageService.createOrUpdate(message), ForwardMessageDto.class);
        forwardMessageDto.setUserId(message.getSender().getUserId());
        forwardMessageDto.setMessageId(message.getOriginMessage().getMessageId());
        forwardMessageDto.setChatId(message.getChat().getChatId());
        return forwardMessageDto;
    }
}
