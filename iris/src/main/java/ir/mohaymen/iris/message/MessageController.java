package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.media.MediaService;
import ir.mohaymen.iris.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ChatService chatService;
    private final MediaService mediaService;
    @GetMapping("/get-messages")
    public ResponseEntity<List<Message>> getMessages(@RequestBody PageDto pageDto) {
        if (pageDto.seal - pageDto.floor > 50)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        List<Message> messages = (List<Message>) messageService.getById(pageDto.chatId);
        return new ResponseEntity<>(messages.subList(pageDto.floor, pageDto.seal) , HttpStatus.OK);
    }
    @PostMapping("/send-message")
    public ResponseEntity<Message> sendMessage (@RequestBody MessageDto messageDto) {
        Media media = modelMapper.map(messageDto , Media.class);
        mediaService.createOrUpdate(media);
        Message message = modelMapper.map(messageDto , Message.class);
        message.setMedia(media);
        message.setSendAt(Instant.now());
        return new ResponseEntity<>(messageService.createOrUpdate(message) , HttpStatus.OK);
    }
    @PatchMapping ("/edit-message")
    public ResponseEntity<Message> editMessage (@RequestBody EditMessageDto editMessageDto) {
        Message message = modelMapper.map(editMessageDto , Message.class);
        message.setEditedAt(Instant.now());
        return new ResponseEntity<>(message , HttpStatus.OK);
    }

}
