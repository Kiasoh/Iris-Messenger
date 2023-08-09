package ir.mohaymen.iris.model;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/chats")
public class ChatController {
    private final ChatService chatService;
    public ChatController(ChatService chatService) {this.chatService = chatService}
    @RequestMapping("/add-chat")
    public ResponseEntity<Chat> createChat(@RequestBody Chat chat) {
        try {
            chatService.save (chat);
        }
        catch (Exception e) {return new ResponseEntity<>(chat , HttpStatus.BAD_REQUEST);}
        return new ResponseEntity<>(chat , HttpStatus.OK);
    }
}
