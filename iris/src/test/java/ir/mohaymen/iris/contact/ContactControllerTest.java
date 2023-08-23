//package ir.mohaymen.iris.contact;
//
//import ir.mohaymen.iris.chat.Chat;
//import ir.mohaymen.iris.chat.ChatService;
//import ir.mohaymen.iris.subscription.Subscription;
//import ir.mohaymen.iris.user.User;
//import ir.mohaymen.iris.user.UserService;
//import ir.mohaymen.iris.user.UserServiceImpl;
//import ir.mohaymen.iris.utility.BaseController;
//import lombok.Setter;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@Setter
//@SpringBootTest
//class ContactControllerTest {
//    User user;
//    User user1;
//
//    @Autowired
//    private UserService userService;
//    @BeforeEach
//    void setup(){
//        user = new User();
//        user.setFirstName("kia");
//        user.setPhoneNumber("019237");
//        userService.createOrUpdate(user);
//        user1 = new User();
//        user1.setFirstName("bia");
//        user1.setPhoneNumber("12398");
//        userService.createOrUpdate(user1);
//
//    }
//
//
//    @Test
//    void addContactTest() {
//        BaseController mockBC = mock(BaseController.class);
//        when(mockBC.getUserByToken()).thenReturn(user);
//        GetContactDto gcd = new GetContactDto();
//        gcd.setFirstName("HAHA");
//        gcd.setContactId(user1.getUserId());
//    }
//
//    @Test
//    void getContacts() {
//        System.out.println("asd");
//        assertTrue(true);
//    }
//
//    @Test
//    void getContact() {
//    }
//
//    @Test
//    void isInContact() {
//        assertFalse(isInContact(user , Long.valueOf(5)));
//    }
//
//    public boolean isInContact (User firstUser , Long secondUserId) {
//        if (firstUser.getContacts() == null)
//            return false;
//        return firstUser.getContacts().stream().anyMatch(c -> c.getSecondUser().getUserId() == secondUserId);
//    }
//
//    @Test
//    void getContactFromList() {
//    }
//}