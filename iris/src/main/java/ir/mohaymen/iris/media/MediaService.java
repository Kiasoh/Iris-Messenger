package ir.mohaymen.iris.media;

public interface MediaService {

    Media getById(Long id);

    Iterable<Media> getAll();

    Media createOrUpdate(Media media);

    void deleteById(Long id);

    void delete(Media media);
}
