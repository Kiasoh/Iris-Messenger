package ir.mohaymen.iris.file;

import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.media.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
public class FileServiceImpl implements FileService {

    private final MediaService mediaService;

    @Value("${application.files.path}")
    private String path = "files";

    @Override
    public Long saveFile(String inputFileName, MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(inputFileName);
        Path uploadPath = Paths.get(path);

        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        Long id;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Media savedMedia = mediaService.createOrUpdate(Media.builder()
                    .fileMimeType(multipartFile.getContentType())
                    .fileName(fileName)
                    .filePath("/api/media/download/")
                    .build());

            savedMedia.setFilePath("/api/media/download/" + savedMedia.getMediaId());
            savedMedia = mediaService.createOrUpdate(savedMedia);
            id = savedMedia.getMediaId();
            String fileCode = generateFileCodeByMediaId(savedMedia.getMediaId());
            String savedFileName = fileCode + "-" + fileName;
            Path filePath = uploadPath.resolve(savedFileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }

        return id;
    }

    private String generateFileCodeByMediaId(Long id) {
        return Long.toString(1000_000L + id);
    }


    @Override
    public Resource getFileAsResource(Long id) throws IOException {
        String fileCode = generateFileCodeByMediaId(id);
        Path dirPath = Paths.get(path);

        Path foundFile = Files.list(dirPath).
                filter(file -> file.getFileName().startsWith(fileCode))
                .findFirst()
                .orElse(null);

        if (foundFile != null) return new UrlResource(foundFile.toUri());
        else return null;
    }
}
