package by.geron.scanner.service.businessLog;

import by.geron.scanner.entity.Acting;
import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.repository.businessLog.BusinessLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessLogApiService implements BusinessLogService{

    private final BusinessLogRepository businessLogRepository;

    @Override
    public BusinessLog saveCreatedBusinessLog(FileObject fileObject) {
        BusinessLog businessLog = buildCreatedBusinessLog(fileObject);
        return saveBusinessLog(businessLog);
    }

    @Override
    public BusinessLog saveUpdatedBusinessLog(FileObject fileObject, FileObject fileObjectDb) {
        BusinessLog businessLog = buildUpdatedBusinessLog(fileObject, fileObjectDb);
        return saveBusinessLog(businessLog);
    }

    @Override
    public BusinessLog saveDeletedBusinessLog(FileObject fileObject) {
        BusinessLog businessLog = buildDeletedBusinessLog(fileObject);
        return saveBusinessLog(businessLog);
    }

    private BusinessLog saveBusinessLog(BusinessLog businessLog) {
        return businessLogRepository.save(businessLog);
    }

    private BusinessLog buildDeletedBusinessLog(FileObject fileObject) {
        return BusinessLog.builder()
                .fileObjectType(fileObject.getType())
                .fileObjectId(fileObject.getId())
                .acting(Acting.DELETED)
                .oldName(fileObject.getName())
                .newName(fileObject.getName())
                .logDateTime(LocalDateTime.now().withNano(0))
                .build();
    }

    private BusinessLog buildUpdatedBusinessLog(FileObject fileObject, FileObject fileObjectDb) {
        return BusinessLog.builder()
                .fileObjectType(fileObject.getType())
                .fileObjectId(fileObjectDb.getId())
                .acting(Acting.UPDATED)
                .oldName(fileObjectDb.getName())
                .newName(fileObject.getName())
                .logDateTime(LocalDateTime.now().withNano(0))
                .build();
    }


    private BusinessLog buildCreatedBusinessLog(FileObject fileObject) {
        return BusinessLog.builder()
                .fileObjectType(fileObject.getType())
                .fileObjectId(fileObject.getId())
                .acting(Acting.CREATED)
                .oldName(fileObject.getName())
                .newName(fileObject.getName())
                .logDateTime(LocalDateTime.now().withNano(0))
                .build();
    }
}
