package by.geron.scanner.service.scan.FS;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.request.ScanRequest;

import java.io.IOException;
import java.util.List;

public interface ScanFSService {

    List<String> scan(ScanRequest request) throws IOException;

    List<String> scan(PathRequest request) throws IOException;

}
