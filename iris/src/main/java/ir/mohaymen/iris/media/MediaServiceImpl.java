package ir.mohaymen.iris.media;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MediaServiceImpl implements MediaService {

    private MediaRepository mediaRepository;

    @Override
    public Media getById(Long id) {
        return mediaRepository.findById(id).orElse(null);
    }

    @Override
    public Media createOrUpdate(Media media) {
        return mediaRepository.save(media);
    }

    @Override
    public void deleteById(Long id) throws Exception {
        if (getById(id) != null) mediaRepository.deleteById(id);
        else throw new Exception("No such file exists.");
    }

    @Override
    public void delete(Media media) {
        mediaRepository.delete(media);
    }
}
