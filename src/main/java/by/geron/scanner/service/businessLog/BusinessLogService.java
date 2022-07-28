package by.geron.scanner.service.businessLog;

import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.entity.FileObject;

public interface BusinessLogService {

    BusinessLog saveCreatedBusinessLog(FileObject fileObject);

    BusinessLog saveUpdatedBusinessLog(FileObject fileObject, FileObject fileObjectDb);

    BusinessLog saveDeletedBusinessLog(FileObject fileObject);
}
