package by.geron.scanner.service.businessLog;

import by.geron.scanner.dto.request.ActingUserRequest;
import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.entity.FileObject;

import java.time.LocalDateTime;
import java.util.List;

public interface BusinessLogService {

    BusinessLog saveCreatedBusinessLog(FileObject fileObject);

    BusinessLog saveUpdatedBusinessLog(FileObject fileObject, FileObject fileObjectDb);

    BusinessLog saveDeletedBusinessLog(FileObject fileObject);

    List<BusinessLog> findAllBusinessLog(LocalDateTime startLogDateTime, LocalDateTime finishLogDateTime);
}
