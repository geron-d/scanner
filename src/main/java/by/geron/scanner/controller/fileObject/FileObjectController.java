package by.geron.scanner.controller.fileObject;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.request.ScanRequest;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.service.fileObject.FileObjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/scanner")
@RequiredArgsConstructor
public class FileObjectController {

    private final FileObjectService fileObjectService;

    @PostMapping("scanWithExtensions")
    public List<FileObject> scan(@RequestBody @Valid ScanRequest request) throws IOException {
        return fileObjectService.scan(request);
    }

    @PostMapping("scan")
    public List<FileObject> scan(@RequestBody @Valid PathRequest request) throws IOException {
        return fileObjectService.scan(request);
    }
}
