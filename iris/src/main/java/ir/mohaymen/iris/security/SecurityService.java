package ir.mohaymen.iris.security;

public interface SecurityService {
    public boolean hasAccessToMedia(long userId, long mediaId);
}
