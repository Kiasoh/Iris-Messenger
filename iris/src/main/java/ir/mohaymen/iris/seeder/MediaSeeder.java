package ir.mohaymen.iris.seeder;

import ir.mohaymen.iris.file.FileMultipartFile;
import ir.mohaymen.iris.file.FileService;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.media.MediaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class MediaSeeder implements Seeder {

    private final MediaRepository mediaRepository;
    @Value("${application.resource.path}")
    private String resourcePath;
    static final int NUMBER_OF_INSTANCES = UserProfileSeeder.NUMBER_OF_INSTANCES + ChatProfileSeeder.NUMBER_OF_INSTANCES
            + MessageSeeder.NUMBER_OF_INSTANCES + 140;
    static final Set<Long> mediaIds = new HashSet<>();
    static int NUMBER_OF_USED_MEDIAS = 0;
    private final List<Media> medias = new ArrayList<>();
    private List<Path> filePaths;
    private final FileService fileService;

    @Override
    public void load() {
        if (mediaRepository.count() != 0) return;

        deleteFiles();
        readFiles();
        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomMedia();

        mediaRepository.saveAll(medias);
        clearReferences();
    }

    @Override
    public void clearReferences() {
        medias.clear();
        filePaths.clear();
    }
    public void deleteFiles(){
        try {
            fileService.deleteAllMedia();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void readFiles() {
        String path =resourcePath + "/images";
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            filePaths = paths.filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateRandomMedia() {
        Integer index = faker.random().nextInt(0, filePaths.size() - 1);

        Path path = filePaths.get(index);
        MultipartFile multipartFile = new FileMultipartFile(path);

        Media media = null;
        try {
            media = fileService.saveFile(multipartFile.getOriginalFilename(), multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (media == null) {
            return;
        }
        medias.add(media);
    }
}
