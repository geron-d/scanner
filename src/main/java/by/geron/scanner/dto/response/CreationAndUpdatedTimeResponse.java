package by.geron.scanner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CreationAndUpdatedTimeResponse {

    @NotNull
    private LocalDateTime creationTime;

    @NotNull
    private LocalDateTime updatedTime;

}
