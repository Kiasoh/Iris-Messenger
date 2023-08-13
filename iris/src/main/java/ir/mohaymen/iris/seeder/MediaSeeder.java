package ir.mohaymen.iris.seeder;

import com.github.javafaker.File;
import ir.mohaymen.iris.media.Media;
import ir.mohaymen.iris.media.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MediaSeeder implements Seeder {

    private final MediaRepository mediaRepository;

    @Override
    public void load() {
        final int NUMBER_OF_INSTANCES = 200;

        for (int i = 0; i < NUMBER_OF_INSTANCES; i++) {
            Media media = generateRandomUser();
            mediaRepository.save(media);
        }
    }

    private Media generateRandomUser() {
        long id = Long.parseLong(fakeValuesService.regexify("\\d{1,5}"));

        File file = faker.file();
        String name = file.fileName();
        String mimeType = file.mimeType();
        String path = fakeValuesService.letterify("./?????/???????");

        Media media = new Media();
        media.setMediaId(id);
        media.setFileName(name);
        media.setFileMimeType(mimeType);
        media.setFilePath(path);
        return media;
    }
}
