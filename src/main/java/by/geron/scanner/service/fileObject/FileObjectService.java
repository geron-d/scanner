package by.geron.scanner.service.fileObject;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.request.ScanRequest;
import by.geron.scanner.entity.FileObject;

import java.io.IOException;
import java.util.List;

public interface FileObjectService {

    List<FileObject> scan(ScanRequest request) throws IOException;

    List<FileObject> scan(PathRequest request) throws IOException;

}
