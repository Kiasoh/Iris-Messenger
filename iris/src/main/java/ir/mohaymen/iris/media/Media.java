package ir.mohaymen.iris.media;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "medias")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaId;

    @NotBlank
    private String fileName;

    private String fileMimeType;

    @NotBlank
    private String filePath;
}
