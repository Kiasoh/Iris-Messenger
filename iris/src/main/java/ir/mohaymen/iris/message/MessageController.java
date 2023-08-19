package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.chat.ChatType;
import ir.mohaymen.iris.contact.Contact;
import ir.mohaymen.iris.contact.ContactService;
import ir.mohaymen.iris.file.FileService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
    private final FileService fileService;

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
        for (Message message : messages.subList(messages.size() - ceil , messages.size() - floor)) {
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

    @RequestMapping(path = "/send-message", method = POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GetMessageDto> sendMessage(@RequestBody @Valid MessageDto messageDto,@RequestPart("file") MultipartFile file) throws IOException {
        Chat chat = chatService.getById(messageDto.getChatId());
        User user = getUserByToken();
        if (!chatService.isInChat(chat, user))
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        Media media;
        if (file==null || file.isEmpty()){
            media=null;
            if (messageDto.getText().isBlank()){
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }
        }
        else {
            var mediaId=fileService.saveFile(file.getOriginalFilename(),file);
            media=Media.builder().mediaId(mediaId).build();
        }
        Message message = new Message();
        message.setText(messageDto.getText());
        message.setSender(user);
        message.setMedia(media);
        message.setSendAt(Instant.now());
//        GetMessageDto getMessageDto = modelMapper.map(messageService.createOrUpdate(message);
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

    private GetMessageDto mapMessageToGetMessageDto(Message message) {
        GetMessageDto getMessageDto = modelMapper.map(messageService.createOrUpdate(message), GetMessageDto.class);
        getMessageDto.setUserId(message.getSender().getUserId());
        return getMessageDto;
    }
}
