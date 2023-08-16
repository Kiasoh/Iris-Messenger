package ir.mohaymen.iris.security;

public interface SecurityService {

    boolean hasAccessToMedia(long userId, long mediaId);
}
