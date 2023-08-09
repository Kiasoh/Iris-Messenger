package ir.mohaymen.iris.utility;

import org.springframework.security.core.context.SecurityContextHolder;

import ir.mohaymen.iris.user.User;

public class BaseController {

    public User getUserByToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

}