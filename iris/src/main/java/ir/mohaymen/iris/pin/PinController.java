package ir.mohaymen.iris.pin;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.message.MessageService;
import ir.mohaymen.iris.user.UserService;
import ir.mohaymen.iris.utility.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/pins")
@RequiredArgsConstructor
public class PinController extends BaseController {
    private final MessageService messageService;
    private final ChatService chatService;
    private final UserService userService;
    private final PinService pinService;

    @PostMapping("/pin-message")
    public ResponseEntity<Pin> pinMessage (@RequestBody PinDto pinDto) {
        Chat chat = chatService.getById(pinDto.getChatId());
        Message message = messageService.getById(pinDto.getMessageId());
        if(!chatService.isInChat(chat , getUserByToken()) || message.getOriginChat()!=chat)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        Pin pin = new Pin();
        pin.setUser(getUserByToken());
        pin.setMessage(message);
        pin.setChat(chat);
        pinService.createOrUpdate(pin);
        return new ResponseEntity<Pin>(pin , HttpStatus.OK);
    }
    @GetMapping("/pinned-messages-of-one-chat")
    public ResponseEntity<List<Pin>> allPinnedMessagesOfOneChat(@RequestBody Long chatId) {
        if(!chatService.isInChat(chatService.getById(chatId) , getUserByToken()))
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>((List<Pin>) pinService.getByChat(chatService.getById(chatId)) , HttpStatus.OK);
    }
}
