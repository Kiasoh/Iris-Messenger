package ir.mohaymen.iris.file;

import ir.mohaymen.iris.utility.CodeGenerator;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public interface FileService {
    String saveFile(String fileName, MultipartFile multipartFile) throws IOException;
    Resource getFileAsResource(String fileCode) throws IOException;

}
