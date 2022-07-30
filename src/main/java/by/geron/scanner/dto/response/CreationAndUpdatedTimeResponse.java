package by.geron.scanner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CreationAndUpdatedTimeResponse {

    private LocalDateTime creationTime;

    private LocalDateTime updatedTime;

}
