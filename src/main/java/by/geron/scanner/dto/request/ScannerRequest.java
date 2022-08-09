package by.geron.scanner.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ScannerRequest {

    @NotBlank
    private String path;

    private List<String> extensions;

}
