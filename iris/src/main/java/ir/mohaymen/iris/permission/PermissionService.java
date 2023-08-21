package ir.mohaymen.iris.permission;

import java.util.Set;

public interface PermissionService {
    boolean hasAccess(long userId,long chatId,Permission permission);
    Set<Permission> getPermissions(Long subId, Long userId);
    Set<Permission> updatePermissions(Long subId, Long userId,Set<Permission> permissions);

}
