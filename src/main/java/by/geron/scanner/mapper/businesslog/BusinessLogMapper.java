package by.geron.scanner.mapper.businesslog;

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

    default BusinessLog fileObjectToDeletedBusinessLog(FileObject fileObject) {
        return BusinessLog.builder()
                .fileObjectType(fileObject.getType())
                .fileObjectId(fileObject.getId())
                .acting(Acting.DELETED)
                .oldName(fileObject.getName())
                .newName(fileObject.getName())
                .logDateTime(LocalDateTime.now().withNano(0))
                .build();
    }

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

    default BusinessLog fileObjectsToRenamedBusinessLog(FileObject fileObject, FileObject fileObjectDb) {
        return BusinessLog.builder()
                .fileObjectType(fileObject.getType())
                .fileObjectId(fileObjectDb.getId())
                .acting(Acting.RENAMED)
                .oldName(fileObjectDb.getName())
                .newName(fileObject.getName())
                .logDateTime(LocalDateTime.now().withNano(0))
                .build();
    }

}
