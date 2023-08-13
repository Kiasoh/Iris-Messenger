package ir.mohaymen.iris.profile;

public class ProfileMapper {

    public static ProfileDto mapToProfileDto(UserProfile userProfile){
        ProfileDto profileDto = new ProfileDto(
                userProfile.getId(),
                userProfile.getMedia(),
                userProfile.getSetAt()
        );
        return profileDto;
    }

    public static ProfileDto mapToProfileDto(ChatProfile chatProfile){
        ProfileDto profileDto = new ProfileDto(
                chatProfile.getId(),
                chatProfile.getMedia(),
                chatProfile.getSetAt()
        );
        return profileDto;
    }
}
