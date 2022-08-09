package by.geron.scanner.mapper.pathscannerstatisticresponse;

import by.geron.scanner.dto.response.PathScannerStatisticResponse;
import by.geron.scanner.entity.Type;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface PathScannerStatisticResponseMapper {

    @Mapping(target = "typeStat", source = "typeStat")
    @Mapping(target = "extensionStat", source = "extensionStat")
    PathScannerStatisticResponse mapsToPathScanStatResponseMapper(Map<Type, Integer> typeStat,
                                                                  Map<String, Integer> extensionStat);

}
