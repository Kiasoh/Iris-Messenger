package ir.mohaymen.iris.contact;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.subscription.Subscription;
import ir.mohaymen.iris.user.User;
import ir.mohaymen.iris.user.UserService;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@Setter
class ContactControllerTest {
    User user;
    @Autowired
    private UserService userService;
    @BeforeEach
    void setup(){
        user = new User();
        user.setFirstName("kia");
        user.setPhoneNumber("019237");
        userService.createOrUpdate(user);
    }


    @Test
    void addContact() {
    }

    @Test
    void getContacts() {
    }

    @Test
    void getContact() {
    }

    @Test
    void isInContact() {
        assertFalse(isInContact(user , Long.valueOf(5)));
    }

    public boolean isInContact (User firstUser , Long secondUserId) {
        if (firstUser.getContacts() == null)
            return false;
        return firstUser.getContacts().stream().anyMatch(c -> c.getSecondUser().getUserId() == secondUserId);
    }

    @Test
    void getContactFromList() {
    }
}