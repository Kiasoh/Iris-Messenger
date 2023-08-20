package ir.mohaymen.iris.chat;

import ir.mohaymen.iris.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByLink(String Link);

    Iterable<Chat> findByTitle(String title);

//    @Query(value = """
//    select new ir.mohaymen.iris.chat.MenuChatDto(
//    c.chatId,
//    c.title,
//    null,
//    m.text,
//
//    )
//    from User u join Subscription sub on u.userId=sub.user.userId
//    join Chat c on sub.chat.chatId=c.chatId
//    join Message m on c.chatId=m.originChat.chatId
//    where u.userId=:userId
//    order by m.messageId desc
//""")
//    List<MenuChatDto> findAllChatWithLastMessage(long userId);
//    @Query(value = """
//
//""")
//    Optional<Message> findLastMessage(long chatId);
}
