package by.geron.scanner.mapper.file;

import org.mapstruct.Mapper;

import java.io.File;

@Mapper(componentModel = "spring")
public interface FileMapper {

    default File pathToFile(String path) {
        return new File(path);
    }

}
