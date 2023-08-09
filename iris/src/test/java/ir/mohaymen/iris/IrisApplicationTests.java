package ir.mohaymen.iris;

import ir.mohaymen.iris.chat.Chat;
import ir.mohaymen.iris.chat.ChatService;
import ir.mohaymen.iris.chat.ChatType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IrisApplicationTests {
	ChatService chatService;
	@Test
	void contextLoads() {
		Chat chat1 = new Chat();
		Chat chat2 = new Chat();
		Chat chat3 = new Chat();
		chat1.setChatType(ChatType.PV);
		chat2.setChatType(ChatType.GROUP);
		chat3.setChatType(ChatType.CHANNEL);
		chatService.createOrUpdate(chat1);
	}

}
