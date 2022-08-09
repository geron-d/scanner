package by.geron.scanner.service.scanner.filesystem;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.request.ScannerRequest;

import java.io.IOException;
import java.util.List;

public interface ScannerFileSystemService {

    List<String> scan(ScannerRequest request) throws IOException;

    List<String> scan(PathRequest request) throws IOException;

}
