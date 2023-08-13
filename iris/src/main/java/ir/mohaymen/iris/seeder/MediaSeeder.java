package ir.mohaymen.iris.seeder;

import com.github.javafaker.File;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.media.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MediaSeeder implements Seeder {

    private final MediaRepository mediaRepository;

    @Override
    public void load() {
        if (mediaRepository.count() != 0) return;

        final int NUMBER_OF_INSTANCES = 200;
        final List<Media> medias = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++)
            generateRandomMedia(medias);
        mediaRepository.saveAll(medias);
    }

    private void generateRandomMedia(List<Media> mediaList) {
        File file = faker.file();
        String name = file.fileName();
        String mimeType = file.mimeType();
        String path = fakeValuesService.letterify("./?????/???????");

        Media media = new Media();
        media.setFileName(name);
        media.setFileMimeType(mimeType);
        media.setFilePath(path);

        mediaList.add(media);
    }
}
