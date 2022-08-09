package by.geron.scanner.dto.response;

import by.geron.scanner.entity.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class PathScannerStatisticResponse {

    private Map<Type, Integer> typeStat;

    private Map<String, Integer> extensionStat;

}
