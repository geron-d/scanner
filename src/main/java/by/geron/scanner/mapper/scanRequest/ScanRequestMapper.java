package by.geron.scanner.mapper.scanRequest;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.request.ScanRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", imports = {List.class})
public interface ScanRequestMapper {

    ScanRequest pathAndExtensionsToScanRequest(String path, List<String> extensions);

    @Mapping(target = "extensions", expression = "java(List.of())")
    ScanRequest pathRequestToScanRequest(PathRequest request);
}
