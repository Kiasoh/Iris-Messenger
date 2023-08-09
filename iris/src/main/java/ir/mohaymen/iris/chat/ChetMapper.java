package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.user.User;

public class ChetMapper {
    public static Chat toChat (ChatDto chatDto) {
        return new Chat(null , chatDto.getTitle(), chatDto.getBio(), chatDto.getLink(), chatDto.isPublic(), chatDto.getChatType());
    }
    public static User toUser (ChatDto chatDto) {
        return new User(null , user);
    }

}
