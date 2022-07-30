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
public class BusinessLog {

    @Id
    private String id;

    private Type fileObjectType;

    private String fileObjectId;

    private Acting acting;

    private String oldName;

    private String newName;

    private LocalDateTime logDateTime;
}
