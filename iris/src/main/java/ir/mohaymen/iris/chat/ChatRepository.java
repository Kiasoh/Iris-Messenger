package ir.mohaymen.iris.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByLink(String Link);

    Iterable<Chat> findByTitle(String title);

    @Query("""
                select c.chatType
                from Chat c
                where c.chatId=:id
            """)
    ChatType getChatType(@Param("id") Long id);

    // @Query(value = """
    // select new ir.mohaymen.iris.chat.MenuChatDto(
    // c.chatId,
    // c.title,
    // null,
    // m.text,
    //
    // )
    // from User u join Subscription sub on u.userId=sub.user.userId
    // join Chat c on sub.chat.chatId=c.chatId
    // join Message m on c.chatId=m.chat.chatId
    // where u.userId=:userId
    // order by m.messageId desc
    // """)
    // List<MenuChatDto> findAllChatWithLastMessage(long userId);
    // @Query(value = """
    //
    // """)
    // Optional<Message> findLastMessage(long chatId);
}
