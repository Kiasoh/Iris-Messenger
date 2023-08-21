package ir.mohaymen.iris.permission;

import ir.mohaymen.iris.chat.Chat;
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
        Chat chat = new Chat();
        chat.setChatId(chatId);
        User user = new User();
        user.setUserId(userId);
        var sub = subscriptionRepository.findByChatAndUser(chat, user).orElseThrow();
        var permissions=sub.getPermissions();
        if (permissions==null) permissions=new HashSet<>();
        return permissions.contains(permission);
    }

    public Set<Permission> getPermissions(Long subId, Long userId) {
        var sub = subscriptionRepository.findById(subId).orElseThrow();
        var permissions = sub.getPermissions();
        if (permissions==null) permissions=new HashSet<>();
        if (!sub.getUser().getUserId().equals(userId) && !Permission.isAdmin(permissions))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return permissions;
    }

    @Override
    public Set<Permission> updatePermissions(Long subId, Long userIdDoer, Set<Permission> newPermissions) {
        var subDoe = subscriptionRepository.findById(subId).orElseThrow();
        var subDoer = subscriptionRepository.findByChatAndUser(subDoe.getChat(), User.builder().userId(userIdDoer).build()).orElseThrow();
        var subDoePermissions = subDoe.getPermissions();
        var subDoerPermissions = subDoer.getPermissions();
        if (subDoePermissions == null) subDoePermissions = new HashSet<>();
        if (subDoerPermissions == null) subDoerPermissions = new HashSet<>();
        if (Permission.isOwner(subDoePermissions)
                || (Permission.isAdmin(subDoePermissions) && Permission.isAdmin(subDoerPermissions))
                || !Permission.isAdmin(subDoerPermissions))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        subDoe.setPermissions(newPermissions);
        var savedSub = subscriptionRepository.save(subDoe);
        return savedSub.getPermissions();
    }
}
