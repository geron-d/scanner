package by.geron.scanner.service.scanner.database;

import by.geron.scanner.dto.request.ScannerRequest;
import by.geron.scanner.entity.FileObject;

import java.io.IOException;
import java.util.List;

public interface ScannerDatabaseService {

    List<String> scanDatabase(ScannerRequest request) throws IOException;

    List<FileObject> scanDatabase(String path) throws IOException;

}
