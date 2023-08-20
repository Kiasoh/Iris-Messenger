package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.profile.ProfileDto;
import ir.mohaymen.iris.profile.ProfileMapper;
import ir.mohaymen.iris.user.User;

import java.util.ArrayList;
import java.util.List;

public class ChatMapper {
    public static GetChatDto mapToGetChatDto (Chat chat) {
        return new GetChatDto();
    }
}
