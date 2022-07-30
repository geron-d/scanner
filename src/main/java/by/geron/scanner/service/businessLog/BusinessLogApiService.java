package by.geron.scanner.service.businessLog;

import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.mapper.businessLog.BusinessLogMapper;
import by.geron.scanner.repository.businessLog.BusinessLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessLogApiService implements BusinessLogService{

    private final BusinessLogRepository businessLogRepository;

    private final BusinessLogMapper businessLogMapper;

    @Override
    public BusinessLog saveCreatedBusinessLog(FileObject fileObject) {
        BusinessLog businessLog = businessLogMapper.fileObjectToCreatedBusinessLog(fileObject);
        return saveBusinessLog(businessLog);
    }

    @Override
    public BusinessLog saveUpdatedBusinessLog(FileObject fileObject, FileObject fileObjectDb) {
        BusinessLog businessLog = businessLogMapper.fileObjectsToUpdatedBusinessLog(fileObject, fileObjectDb);
        return saveBusinessLog(businessLog);
    }

    @Override
    public BusinessLog saveDeletedBusinessLog(FileObject fileObject) {
        BusinessLog businessLog = businessLogMapper.fileObjectToDeletedBusinessLog(fileObject);
        return saveBusinessLog(businessLog);
    }

    private BusinessLog saveBusinessLog(BusinessLog businessLog) {
        return businessLogRepository.save(businessLog);
    }

}
