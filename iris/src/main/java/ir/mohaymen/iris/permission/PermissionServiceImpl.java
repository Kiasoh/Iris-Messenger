package ir.mohaymen.iris.permission;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.subscription.SubscriptionRepository;
import ir.mohaymen.iris.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService{
    private final SubscriptionRepository subscriptionRepository;
    private boolean test=true;
    public boolean hasAccess(long userId,long chatId,Permission permission){
        if (test) return true;
        Chat chat=new Chat();
        chat.setChatId(chatId);
        User user=new User();
        user.setUserId(userId);
        var sub= subscriptionRepository.findByChatAndUser(chat,user).orElseThrow();
        return sub.getPermissions().contains(permission);
    }
}
