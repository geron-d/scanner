package by.geron.scanner.mapper.scannerrequest;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.request.ScannerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", imports = {List.class})
public interface ScannerRequestMapper {

    ScannerRequest pathAndExtensionsToScanRequest(String path, List<String> extensions);

    @Mapping(target = "extensions", expression = "java(List.of())")
    ScannerRequest pathRequestToScanRequest(PathRequest request);

    @Mapping(target = "extensions", expression = "java(List.of())")
    ScannerRequest pathToScanRequest(String path);
}
