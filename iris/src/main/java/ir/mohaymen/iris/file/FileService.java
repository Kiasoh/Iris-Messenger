package ir.mohaymen.iris.file;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    Long saveFile(String fileName, MultipartFile multipartFile) throws IOException;
    Resource getFileAsResource(Long id) throws IOException;
}
