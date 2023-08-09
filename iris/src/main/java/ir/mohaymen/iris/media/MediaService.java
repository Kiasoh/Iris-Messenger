package ir.mohaymen.iris.media;

public interface MediaService {

    Media getById(Long id);

    Media createOrUpdate(Media media);

    void deleteById(Long id) throws Exception;

    void delete(Media media);
}
