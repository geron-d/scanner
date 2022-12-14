package by.geron.scanner.service.businesslog;

import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.mapper.businesslog.BusinessLogMapper;
import by.geron.scanner.repository.businesslog.BusinessLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessLogApiService implements BusinessLogService {

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

    @Override
    public BusinessLog saveRenamedBusinessLog(FileObject fileObject, FileObject fileObjectDb) {
        BusinessLog businessLog = businessLogMapper.fileObjectsToRenamedBusinessLog(fileObject, fileObjectDb);
        return saveBusinessLog(businessLog);
    }

    @Override
    public List<BusinessLog> findAllBusinessLog(LocalDateTime startLogDateTime, LocalDateTime finishLogDateTime,
                                                Pageable pageable) {
        return businessLogRepository.findAllByLogDateTimeBetween(startLogDateTime, finishLogDateTime, pageable)
                .getContent();
    }

    @Override
    public List<BusinessLog> findAllBusinessLog(LocalDateTime startLogDateTime, Pageable pageable) {
        return businessLogRepository.findAllByLogDateTimeAfter(startLogDateTime, pageable).getContent();
    }

    private BusinessLog saveBusinessLog(BusinessLog businessLog) {
        return businessLogRepository.save(businessLog);
    }

}
