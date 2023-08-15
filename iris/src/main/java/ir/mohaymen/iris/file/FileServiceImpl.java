package ir.mohaymen.iris.file;

import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.media.MediaService;
import ir.mohaymen.iris.utility.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{
    private final MediaService mediaService;
    public String saveFile(String inputFileName, MultipartFile multipartFile)
            throws IOException {
        var fileName=StringUtils.cleanPath(inputFileName);
        Path uploadPath = Paths.get("files");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileCode;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            var savedMedia=mediaService.createOrUpdate(Media.builder()
                            .fileMimeType(multipartFile.getContentType())
                            .fileName(fileName)
                            .filePath("/api/media/download/")
                    .build());
            savedMedia.setFilePath("/api/media/download/"+savedMedia.getMediaId());
            savedMedia=mediaService.createOrUpdate(savedMedia);
            fileCode=savedMedia.getMediaId().toString();
            String savedFileName=fileCode + "-" + fileName;
            Path filePath = uploadPath.resolve(savedFileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }

        return fileCode;
    }
    private Path foundFile;

    public Resource getFileAsResource(String fileCode) throws IOException {
        Path dirPath = Paths.get("files");

        Files.list(dirPath).forEach(file -> {
            if (file.getFileName().toString().startsWith(fileCode)) {
                foundFile = file;
                return;
            }
        });

        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }

        return null;
    }
}
