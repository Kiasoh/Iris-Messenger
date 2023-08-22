package ir.mohaymen.iris.permission;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.message.Message;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.subscription.SubscriptionRepository;
import ir.mohaymen.iris.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final SubscriptionRepository subscriptionRepository;
    private boolean test = true;

    public boolean hasAccess(long userId, long chatId, Permission permission) {
        if (test) return true;
        Subscription sub = getSubByUserAndChat(userId, chatId);
        Set<Permission> permissions = getPermissionsBySub(sub);
        return permissions.contains(permission);
    }

    @Override
    public boolean hasAccessToDeleteMessage(Message message, long userId, long chatId) {
        if (test) return true;
        if (message.getSender().getUserId().equals(userId)) return true;
        Subscription sub = getSubByUserAndChat(userId, chatId);
        Subscription subSender = getSubByUserAndChat(message.getSender().getUserId(), chatId);
        var permissions = getPermissionsBySub(sub);
        var permissionsSender = getPermissionsBySub(subSender);
        if (Permission.isOwner(permissions)) return true;
        if (Permission.isAdmin(permissions) && Permission.isAdmin(permissionsSender)) return false;
        return Permission.isAdmin(permissions);
    }

    public Set<Permission> getPermissions(Long subId, Long userId) {
        var sub = subscriptionRepository.findById(subId).orElseThrow();
        Set<Permission> permissions = getPermissionsBySub(sub);
        if (!sub.getUser().getUserId().equals(userId) && !Permission.isAdmin(permissions))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return permissions;
    }

    @Override
    public Set<Permission> updatePermissions(Long subId, Long userIdDoer, Set<Permission> newPermissions) {
        var subDoe = subscriptionRepository.findById(subId).orElseThrow();
        var subDoer = subscriptionRepository.findByChatAndUser(subDoe.getChat(), User.builder().userId(userIdDoer).build()).orElseThrow();
        var subDoePermissions = getPermissionsBySub(subDoe);
        var subDoerPermissions = getPermissionsBySub(subDoer);
        if (Permission.isOwner(subDoePermissions)
                || (Permission.isAdmin(subDoePermissions) && Permission.isAdmin(subDoerPermissions))
                || !Permission.isAdmin(subDoerPermissions))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        subDoe.setPermissions(newPermissions);
        var savedSub = subscriptionRepository.save(subDoe);
        return savedSub.getPermissions();
    }

    private Subscription getSubByUserAndChat(long userId, long chatId) {
        Chat chat = new Chat();
        chat.setChatId(chatId);
        User user = new User();
        user.setUserId(userId);
        return subscriptionRepository.findByChatAndUser(chat, user).orElseThrow();
    }

    private Set<Permission> getPermissionsBySub(Subscription sub) {
        var permissions = sub.getPermissions();
        if (permissions == null) permissions = new HashSet<>();
        return permissions;
    }
}
