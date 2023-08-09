package ir.mohaymen.iris.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "medias")
@Data
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
