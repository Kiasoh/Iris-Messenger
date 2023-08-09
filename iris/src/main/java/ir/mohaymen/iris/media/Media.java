package ir.mohaymen.iris.media;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "medias")
@Getter
@Setter
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaId;
    @NotBlank
    private String fileName;
    private String fileContentType;
    @NotBlank
    private String filePath;
}
