package by.geron.scanner.controller.fileObject;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.request.ScanRequest;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.service.scan.ScanService;
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

    private final ScanService scanService;

    @PostMapping("scanWithExtensions")
    public List<String> scan(@RequestBody @Valid ScanRequest request) throws IOException {
        return scanService.scan(request);
    }

    @PostMapping("scan")
    public List<String> scan(@RequestBody @Valid PathRequest request) throws IOException {
        return scanService.scan(request);
    }
}
