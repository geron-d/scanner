package by.geron.scanner.mapper.pathScanStatResponse;

import by.geron.scanner.dto.response.PathScanStatResponse;
import by.geron.scanner.entity.Type;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface PathScanStatResponseMapper {

    @Mapping(target = "typeStat", source = "typeStat")
    @Mapping(target = "extensionStat", source = "extensionStat")
    PathScanStatResponse mapsToPathScanStatResponseMapper(Map<Type, Integer> typeStat,
                                                          Map<String, Integer> extensionStat);

}
