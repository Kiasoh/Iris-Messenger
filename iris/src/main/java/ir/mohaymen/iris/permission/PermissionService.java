package ir.mohaymen.iris.permission;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.message.Message;

import java.util.Set;

public interface PermissionService {
    boolean hasAccess(long userId,long chatId,Permission permission);
    boolean isOwner(long userId,Chat chat);
    boolean isOwner(long userId,long chatId);
    boolean hasAccessToDeleteMessage(Message message, long userId, Chat chat);
    Set<Permission> getPermissions(Long subId, Long userId);
    Set<Permission> updatePermissions(Long subId, Long userId,Set<Permission> permissions);

}
