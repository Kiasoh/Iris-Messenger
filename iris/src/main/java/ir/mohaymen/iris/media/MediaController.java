package ir.mohaymen.iris.media;

import ir.mohaymen.iris.file.FileService;
import ir.mohaymen.iris.security.SecurityService;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.utility.BaseController;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.MessageFormat;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media")
public class MediaController extends BaseController {

    private final FileService fileService;
    private final MediaService mediaService;
    private final SecurityService securityService;
    private final Logger logger = LoggerFactory.getLogger(MediaController.class);

    @GetMapping("/download/{mediaId}")
    public ResponseEntity<?> downloadFile(@PathVariable Long mediaId) {

        User user = getUserByToken();
        if (!securityService.hasAccessToMedia(user.getUserId(), mediaId)) {
            logger.info(MessageFormat.format("user with phoneNumber:{0} wants to access media id:{1}. not permitted!", user.getPhoneNumber(), mediaId));
            return new ResponseEntity<>("Access violation", HttpStatus.FORBIDDEN);
        }

        Resource resource;
        try {
            resource = fileService.getFileAsResource(mediaId);
            if (resource == null) return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        String contentType = "application/octet-stream";
        String fileName = mediaService.getById(mediaId).getFileName();
        String headerValue = "inline; filename=\"" + fileName + "\"";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }
}
