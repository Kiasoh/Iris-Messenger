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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MediaService mediaService;

    @Value("${application.files.path}")
    private String path = "files";

    @Override
    public Media saveFile(String inputFileName, MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(inputFileName);
        Path uploadPath = Paths.get(path);

        if (!Files.exists(uploadPath))
            Files.createDirectories(uploadPath);

        Media media;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Media savedMedia = mediaService.createOrUpdate(Media.builder()
                    .fileMimeType(multipartFile.getContentType())
                    .fileName(fileName)
                    .filePath("/api/media/download/")
                    .build());

            savedMedia.setFilePath("/api/media/download/" + savedMedia.getMediaId());
            savedMedia = mediaService.createOrUpdate(savedMedia);
            media = savedMedia;
            String fileCode = generateFileCodeByMediaId(savedMedia.getMediaId());
            String savedFileName = fileCode + "-" + fileName;
            Path filePath = uploadPath.resolve(savedFileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }

        return media;
    }

    private String generateFileCodeByMediaId(Long id) {
        return Long.toString(1000_000L + id);
    }

    @Override
    public Resource getFileAsResource(Long id) throws IOException {
        Path foundFile = getPathById(id);

        if (foundFile != null)
            return new UrlResource(foundFile.toUri());
        else
            return null;
    }

    public void deleteFile(Long id) throws IOException {
        Path foundFile = getPathById(id);
        if (foundFile == null)
            Files.delete(foundFile);
    }
    public void deleteAllMedia() throws IOException {
        Path pathToDelete = Paths.get(path);
        try (Stream<Path> pathStream = Files.walk(pathToDelete)) {
            pathStream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }
    @Override
    public Media duplicateMediaById(Media media) {
        Path path = null;
        try {
            path = getPathById(media.getMediaId());
        } catch (IOException e) {
            e.printStackTrace();
        }

        MultipartFile multipartFile = new FileMultipartFile(path, media.getFileName());

        Media duplicateMedia = null;
        try {
            duplicateMedia = saveFile(multipartFile.getOriginalFilename(), multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return duplicateMedia;
    }

    private Path getPathById(Long id) throws IOException {
        String fileCode = generateFileCodeByMediaId(id);
        Path dirPath = Paths.get(path);

        return Files.list(dirPath).filter(file -> file.getFileName().toString().startsWith(fileCode))
                .findFirst()
                .orElse(null);
    }
}
