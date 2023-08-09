package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.subscription.SubscriptionService;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ChatControllerTest {
    @Autowired
    ChatService chatService;
    @Autowired
    SubscriptionService subscriptionService;
    @Autowired
    UserService userService;
    @Test
    void createChat() {
//        try {
        Chat chat1 = new Chat();
        Chat chat2 = new Chat();
        Chat chat3 = new Chat();
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        chat1.setTitle("asd1");chat2.setTitle("1asd1");chat3.setTitle("1asd11");
        user1.setFirstName("asd");
        chat1.setChatType(ChatType.PV);
        chat2.setChatType(ChatType.GROUP);
        chat3.setChatType(ChatType.CHANNEL);
        user1 = userService.createOrUpdate(user1);
        chat1 = chatService.createOrUpdate(chat1); chat2 = chatService.createOrUpdate(chat2);chat3 = chatService.createOrUpdate(chat3);
        Subscription sub = new Subscription(null, userService.getById(user1.getUserId()) , chatService.getById(chat1.getChatId()));
//        Subscription sub1 = new Subscription(null, user1 , chat1);
        subscriptionService.createOrUpdate(sub);



//        }
//        catch (Exception ex) {
//            System.out.println("haha");
//        }



    }
}