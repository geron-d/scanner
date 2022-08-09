package by.geron.scanner.mapper.businesslog;

import by.geron.scanner.entity.Acting;
import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.entity.FileObject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {Acting.class, LocalDateTime.class})
public interface BusinessLogMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fileObjectType", source = "type")
    @Mapping(target = "fileObjectId", source = "id")
    @Mapping(target = "acting", expression = "java(Acting.CREATED)")
    @Mapping(target = "oldName", source = "name")
    @Mapping(target = "newName", source = "name")
    @Mapping(target = "logDateTime", expression = "java(LocalDateTime.now().withNano(0))")
    BusinessLog fileObjectToCreatedBusinessLog(FileObject fileObject);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fileObjectType", source = "type")
    @Mapping(target = "fileObjectId", source = "id")
    @Mapping(target = "acting", expression = "java(Acting.DELETED)")
    @Mapping(target = "oldName", source = "name")
    @Mapping(target = "newName", source = "name")
    @Mapping(target = "logDateTime", expression = "java(LocalDateTime.now().withNano(0))")
    BusinessLog fileObjectToDeletedBusinessLog(FileObject fileObject);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fileObjectType", source = "fileObject.type")
    @Mapping(target = "fileObjectId", source = "fileObjectDb.id")
    @Mapping(target = "acting", expression = "java(Acting.UPDATED)")
    @Mapping(target = "oldName", source = "fileObjectDb.name")
    @Mapping(target = "newName", source = "fileObject.name")
    @Mapping(target = "logDateTime", expression = "java(LocalDateTime.now().withNano(0))")
    BusinessLog fileObjectsToUpdatedBusinessLog(FileObject fileObject, FileObject fileObjectDb);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fileObjectType", source = "fileObject.type")
    @Mapping(target = "fileObjectId", source = "fileObjectDb.id")
    @Mapping(target = "acting", expression = "java(Acting.RENAMED)")
    @Mapping(target = "oldName", source = "fileObjectDb.name")
    @Mapping(target = "newName", source = "fileObject.name")
    @Mapping(target = "logDateTime", expression = "java(LocalDateTime.now().withNano(0))")
    BusinessLog fileObjectsToRenamedBusinessLog(FileObject fileObject, FileObject fileObjectDb);

}
