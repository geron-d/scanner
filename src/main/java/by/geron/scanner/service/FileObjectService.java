package by.geron.scanner.service;

import by.geron.scanner.dto.request.ScanRequest;
import by.geron.scanner.entity.FileObject;

import java.io.IOException;
import java.util.List;

public interface FileObjectService {

    List<FileObject> scan(ScanRequest request) throws IOException;
}
