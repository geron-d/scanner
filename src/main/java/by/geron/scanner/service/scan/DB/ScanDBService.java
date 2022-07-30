package by.geron.scanner.service.scan.DB;

import by.geron.scanner.dto.request.ScanRequest;
import by.geron.scanner.entity.FileObject;

import java.io.IOException;
import java.util.List;

public interface ScanDBService {

    List<String> scanDb(ScanRequest request) throws IOException;

    List<FileObject> scanDb(String path) throws IOException;

}
