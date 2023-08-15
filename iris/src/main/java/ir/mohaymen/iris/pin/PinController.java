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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pins")
@RequiredArgsConstructor
public class PinController extends BaseController {
    private final MessageService messageService;
    private final ChatService chatService;
    private final UserService userService;
    private final PinService pinService;

    @PostMapping("/pin-message")
    public ResponseEntity<GetPinDto> pinMessage (@RequestBody PinDto pinDto) {
        Chat chat = chatService.getById(pinDto.getChatId());
        Message message = messageService.getById(pinDto.getMessageId());
        if(!chatService.isInChat(chat , getUserByToken()) || message.getOriginChat()!=chat)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        Pin pin = new Pin();
        pin.setUser(getUserByToken());
        pin.setMessage(message);
        pin.setChat(chat);
        pin = pinService.createOrUpdate(pin);
        GetPinDto getPinDto = new GetPinDto(); getPinDto.setMessageId(pin.getPinId());
        return new ResponseEntity<GetPinDto>(getPinDto , HttpStatus.OK);
    }
    @GetMapping("/pinned-messages-of-one-chat/{chatId}")
    public ResponseEntity<List<GetPinDto>> allPinnedMessagesOfOneChat(@PathVariable Long chatId) {
        if(!chatService.isInChat(chatService.getById(chatId) , getUserByToken()))
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        List<GetPinDto> getPinDtoList = new ArrayList<>();
        for (Pin pin:pinService.getByChat(chatService.getById(chatId))) {
            GetPinDto getPinDto = new GetPinDto(); getPinDto.setMessageId(pin.getPinId());
            getPinDtoList.add(getPinDto);
        }
        return new ResponseEntity<>(getPinDtoList , HttpStatus.OK);
    }
}
