package ir.mohaymen.iris.search.message;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@AllArgsConstructor
@Document(indexName = "message")
public class SearchMessageDto {

    @Id
    private Long id;

    @Field(type = FieldType.Text, name = "text")
    private String text;

    // @Field(type = FieldType.Nested, includeInParent = true)
    // private Chat chat;
    // @Field(type = FieldType.Nested, includeInParent = true)
    // private User sender;
}
