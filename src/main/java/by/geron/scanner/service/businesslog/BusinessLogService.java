package by.geron.scanner.service.businesslog;

import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.entity.FileObject;

import java.time.LocalDateTime;
import java.util.List;

public interface BusinessLogService {

    BusinessLog saveCreatedBusinessLog(FileObject fileObject);

    BusinessLog saveUpdatedBusinessLog(FileObject fileObject, FileObject fileObjectDb);

    BusinessLog saveDeletedBusinessLog(FileObject fileObject);

    BusinessLog saveRenamedBusinessLog(FileObject fileObject, FileObject fileObjectDb);

    List<BusinessLog> findAllBusinessLog(LocalDateTime startLogDateTime, LocalDateTime finishLogDateTime);

    List<BusinessLog> findAllBusinessLog(LocalDateTime startLogDateTime);

}
