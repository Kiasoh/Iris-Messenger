package ir.mohaymen.iris.permission;

public interface PermissionService {
    boolean hasAccess(long userId,long chatId,Permission permission);

}
