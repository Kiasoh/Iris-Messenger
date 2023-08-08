package ir.mohaymen.iris.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "medias")
@Data
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaId;
    private String fileName;
    private String fileContentType;
    private String filePath;
}
