package ir.mohaymen.iris.message;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.media.MediaService;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.utility.BaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

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
