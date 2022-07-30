package by.geron.scanner.mapper.businessLog;

import by.geron.scanner.entity.Acting;
import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.entity.FileObject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {Acting.class, LocalDateTime.class})
public interface BusinessLogMapper {

    @Mapping(target = "fileObjectType", source = "type")
    @Mapping(target = "fileObjectId", source = "id")
    @Mapping(target = "acting", expression = "java(Acting.CREATED)")
    @Mapping(target = "oldName", source = "name")
    @Mapping(target = "newName", source = "name")
    @Mapping(target = "logDateTime", expression = "java(LocalDateTime.now().withNano(0))")
    BusinessLog fileObjectToCreatedBusinessLog(FileObject fileObject);

    @Mapping(target = "fileObjectType", source = "type")
    @Mapping(target = "fileObjectId", source = "id")
    @Mapping(target = "acting", expression = "java(Acting.DELETED)")
    @Mapping(target = "oldName", source = "name")
    @Mapping(target = "newName", source = "name")
    @Mapping(target = "logDateTime", expression = "java(LocalDateTime.now().withNano(0))")
    BusinessLog fileObjectToDeletedBusinessLog(FileObject fileObject);

    default BusinessLog fileObjectsToUpdatedBusinessLog(FileObject fileObject, FileObject fileObjectDb) {
        return BusinessLog.builder()
                .fileObjectType(fileObject.getType())
                .fileObjectId(fileObjectDb.getId())
                .acting(Acting.UPDATED)
                .oldName(fileObjectDb.getName())
                .newName(fileObject.getName())
                .logDateTime(LocalDateTime.now().withNano(0))
                .build();
    }


}
