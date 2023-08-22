package ir.mohaymen.iris.permission;

import ir.mohaymen.iris.message.Message;

import java.util.Set;

public interface PermissionService {
    boolean hasAccess(long userId,long chatId,Permission permission);
    boolean hasAccessToDeleteMessage(Message message, long userId, long chatId);
    Set<Permission> getPermissions(Long subId, Long userId);
    Set<Permission> updatePermissions(Long subId, Long userId,Set<Permission> permissions);

}
