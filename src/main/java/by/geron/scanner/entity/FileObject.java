package by.geron.scanner.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class FileObject {

    @Id
    private String id;

    private String idParent;

    private String path;

    private String name;

    private String extension;

    private Type type;

    private LocalDateTime creationTime;

    private LocalDateTime updatedTime;

}
