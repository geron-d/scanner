package by.geron.scanner.service.scan;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.request.ScanRequest;
import by.geron.scanner.entity.FileObject;

import java.io.IOException;
import java.util.List;

public interface ScanService {

    List<String> scan(ScanRequest request) throws IOException;

    List<String> scan(PathRequest request) throws IOException;

}
