package ir.mohaymen.iris.media;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;

    @Override
    public Media getById(Long id) {
        return mediaRepository.findById(id).orElse(null);
    }

    @Override
    public Iterable<Media> getAll() {
        return mediaRepository.findAll();
    }

    @Override
    public Media createOrUpdate(Media media) {
        return mediaRepository.save(media);
    }

    @Override
    public void deleteById(Long id) {
        mediaRepository.deleteById(id);
    }

    @Override
    public void delete(Media media) {
        mediaRepository.delete(media);
    }
}
