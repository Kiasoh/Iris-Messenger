package ir.mohaymen.iris.permission;

import ir.mohaymen.iris.chat.ChatType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum Permission {
    SEND_MESSAGE,
    ADD_USER,
    CHANGE_CHAT_INFO,
    PIN_MESSAGE,
    ADMIN,
    OWNER;

    public static Set<Permission> getOwnerPermissions(){
        Set<Permission> permissions=new HashSet<>();
        permissions.addAll(List.of(Permission.values()));
        return permissions;
    }
    public static Set<Permission> getDefaultPermissions(ChatType chatType){
        Set<Permission> permissions=new HashSet<>();
        switch (chatType){

            case PV -> {
                permissions.add(Permission.SEND_MESSAGE);
                permissions.add(Permission.PIN_MESSAGE);
            }
            case GROUP -> {
                permissions.add(Permission.SEND_MESSAGE);
                permissions.add(Permission.ADD_USER);
                permissions.add(Permission.CHANGE_CHAT_INFO);
                permissions.add(Permission.PIN_MESSAGE);
            }
            case CHANNEL -> {
            }
        }
        return permissions;
    }
    public static boolean isAdmin(Set<Permission> permissions){
        return permissions.contains(ADMIN) || permissions.contains(OWNER);
    }
    public static boolean isOwner(Set<Permission> permissions){
        return permissions.contains(OWNER);
    }

}
