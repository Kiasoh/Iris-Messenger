package ir.mohaymen.iris.file;

import ir.mohaymen.iris.media.Media;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileService {

    Media saveFile(String fileName, MultipartFile multipartFile) throws IOException;

    Resource getFileAsResource(Long id) throws IOException;

    void deleteFile(Long id) throws IOException;

    Media duplicateMediaById(Long mediaId);
}
