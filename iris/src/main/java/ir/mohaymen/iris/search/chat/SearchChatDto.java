package ir.mohaymen.iris.search.chat;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@AllArgsConstructor
@Getter
@Setter
@Document(indexName = "chat",createIndex = false)
public class SearchChatDto {

    @Id
    private Long id;

    @Field(type = FieldType.Long)
    private Long userId;

    @Field(type = FieldType.Long)
    private Long chatId;

    @Field(type = FieldType.Text)
    private String title;

}
