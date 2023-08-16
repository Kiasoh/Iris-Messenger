package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.chat.ChatType;
import ir.mohaymen.iris.contact.Contact;
import ir.mohaymen.iris.contact.ContactService;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.media.MediaService;
import ir.mohaymen.iris.subscription.SubDto;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.subscription.SubscriptionService;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.utility.BaseController;
import ir.mohaymen.iris.utility.Nameable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/get-messages/{chatId}/{floor}/{ceil}")
    public ResponseEntity<List<GetMessageDto>> getMessages(@PathVariable("chatId") Long chatId , @PathVariable("floor") Integer floor , @PathVariable("ceil") Integer ceil ) {
        if (ceil - floor > 50)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        List<Message> messages = (List<Message>) messageService.getByChat(chatService.getById(chatId));
        if (messages.size() > ceil)
            ceil = (messages.size());
        if(floor < 0)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        List<GetMessageDto> getMessageDtoList = new ArrayList<>();
        for (Message message:messages.subList(floor, ceil)) {
            getMessageDtoList.add(mapMessageToGetMessageDto(message));
        }
        return new ResponseEntity<>(getMessageDtoList , HttpStatus.OK);
    }
    @GetMapping("/seen-users/{chatId}/{messageId}")
    public ResponseEntity<List<SubDto>> usersSeen (@PathVariable Long chatId , @PathVariable Long messageId) {
        Iterable<Contact> contacts = contactService.getContactByFirstUser(getUserByToken());
        if (chatService.getById(chatId).getChatType() == ChatType.CHANNEL)
            return new ResponseEntity<>(null , HttpStatus.FORBIDDEN);
        List<SubDto> users = new ArrayList<>();
        messageService.usersSeen(messageId , chatId).forEach( s -> {
            SubDto subDto = new SubDto();
            Nameable nameable = subscriptionService.setName( contacts , s.getUser());
            subDto.setFirstName(nameable.getFirstName());
            subDto.setLastName(nameable.getLastName());
            subDto.setUserId(s.getUser().getUserId());
            users.add(subDto);
        });
        return new ResponseEntity<>(users , HttpStatus.OK);
    }
    @GetMapping("/seen-user-count/{chatId}/{messageId}")
    public ResponseEntity<Integer> userSeenCount (@PathVariable Long chatId , @PathVariable Long messageId) {
        return new ResponseEntity<>(messageService.usersSeen(messageId , chatId).size() , HttpStatus.OK);
    }
    @PostMapping("/send-message")
    public ResponseEntity<GetMessageDto> sendMessage (@RequestBody @Valid MessageDto messageDto) {
        Chat chat = chatService.getById(messageDto.getChatId());
        User user = getUserByToken();
        if (!chatService.isInChat(chat ,user ) )
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        Media media;
        if (messageDto.getFileContentType() == null && messageDto.getFileName() == null && messageDto.getFilePath() == null) {
            if (messageDto.getText() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            media = null;
        }
        else {
            media = modelMapper.map(messageDto , Media.class);
            mediaService.createOrUpdate(media);
        }
        Message message = new Message();
        message.setText(messageDto.getText());
        message.setOriginChat(chat);
        message.setSender(user);
        message.setMedia(media);
        message.setSendAt(Instant.now());
//        GetMessageDto getMessageDto = modelMapper.map(messageService.createOrUpdate(message);
        return new ResponseEntity<>(mapMessageToGetMessageDto(message), HttpStatus.OK);
    }
    @PatchMapping ("/edit-message")
    public ResponseEntity<GetMessageDto> editMessage (@RequestBody @Valid EditMessageDto editMessageDto) {
        Message message = messageService.getById(editMessageDto.getMessageId());
        message.setText(editMessageDto.getText());
        message.setEditedAt(Instant.now());
        return new ResponseEntity<>( mapMessageToGetMessageDto(message), HttpStatus.OK);
    }
    private GetMessageDto mapMessageToGetMessageDto(Message message) {
        GetMessageDto getMessageDto = modelMapper.map(messageService.createOrUpdate(message) , GetMessageDto.class);
        getMessageDto.setUserId(message.getSender().getUserId());
        return getMessageDto;
    }
}
