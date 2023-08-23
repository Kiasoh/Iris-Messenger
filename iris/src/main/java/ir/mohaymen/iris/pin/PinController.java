package ir.mohaymen.iris.pin;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.message.MessageService;
import ir.mohaymen.iris.permission.Permission;
import ir.mohaymen.iris.permission.PermissionService;
import ir.mohaymen.iris.user.UserService;
import ir.mohaymen.iris.utility.BaseController;
import jakarta.validation.Valid;
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
    private final PermissionService permissionService;
    @PostMapping("/pin-message")
    public ResponseEntity<?> pinMessage (@RequestBody @Valid PinDto pinDto) {
        if (!permissionService.hasAccess(getUserByToken().getUserId(),pinDto.getChatId(), Permission.PIN_MESSAGE))
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);

        Chat chat = chatService.getById(pinDto.getChatId());
        Message message = messageService.getById(pinDto.getMessageId());
        if(!chatService.isInChat(chat , getUserByToken()) || message.getChat()!=chat)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        Pin pin = new Pin();
        pin.setUser(getUserByToken());
        pin.setMessage(message);
        pin.setChat(chat);
        pinService.createOrUpdate(pin);
        return ResponseEntity.ok("message pinned");
    }

    @GetMapping("/pinned-messages-of-one-chat/{chatId}")
    public ResponseEntity<List<GetPinDto>> allPinnedMessagesOfOneChat(@PathVariable Long chatId) {
        if(!chatService.isInChat(chatService.getById(chatId) , getUserByToken()))
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        List<GetPinDto> getPinDtoList = new ArrayList<>();
        for (Pin pin:pinService.getByChat(chatService.getById(chatId))) {
            GetPinDto getPinDto = new GetPinDto(); getPinDto.setMessagePlacement(messageService.messagePlacementInChat(chatId ,pin.getMessage().getMessageId())); getPinDto.setMessageText(pin.getMessage().getText());
            getPinDtoList.add(getPinDto);
        }
        return new ResponseEntity<>(getPinDtoList , HttpStatus.OK);
    }
}
