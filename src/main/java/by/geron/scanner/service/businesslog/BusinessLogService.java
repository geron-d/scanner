package by.geron.scanner.service.businesslog;

import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.entity.FileObject;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface BusinessLogService {

    BusinessLog saveCreatedBusinessLog(FileObject fileObject);

    BusinessLog saveUpdatedBusinessLog(FileObject fileObject, FileObject fileObjectDb);

    BusinessLog saveDeletedBusinessLog(FileObject fileObject);

    BusinessLog saveRenamedBusinessLog(FileObject fileObject, FileObject fileObjectDb);

    List<BusinessLog> findAllBusinessLog(LocalDateTime startLogDateTime, LocalDateTime finishLogDateTime,
                                         Pageable pageable);

    List<BusinessLog> findAllBusinessLog(LocalDateTime startLogDateTime, Pageable pageable);

}
