package by.geron.scanner.controller.scanner;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.request.ScannerRequest;
import by.geron.scanner.service.scanner.filesystem.ScannerFileSystemService;
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
public class ScannerController {

    private final ScannerFileSystemService scanService;

    @PostMapping("extensions")
    public List<String> scan(@RequestBody @Valid ScannerRequest request) throws IOException {
        return scanService.scan(request);
    }

    @PostMapping
    public List<String> scan(@RequestBody @Valid PathRequest request) throws IOException {
        return scanService.scan(request);
    }
}
