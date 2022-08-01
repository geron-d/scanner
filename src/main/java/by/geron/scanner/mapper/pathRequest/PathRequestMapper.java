package by.geron.scanner.mapper.pathRequest;

import by.geron.scanner.dto.request.PathRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PathRequestMapper {

    PathRequest pathToPathRequest(String path);

}
