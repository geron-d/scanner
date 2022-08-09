package by.geron.scanner.job;

import by.geron.scanner.mapper.pathrequest.PathRequestMapper;
import by.geron.scanner.service.scanner.filesystem.ScannerFileSystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScannerJob {

    private final ScannerFileSystemService scannerFileSystemService;

    private final PathRequestMapper pathRequestMapper;

    @Value("${job.scanJobPath}")
    private String path;

    @Scheduled(fixedRateString = "${job.scanFixedRate}")
    public void scanFileSystem() throws IOException {
        scannerFileSystemService.scan(pathRequestMapper.pathToPathRequest(path));
    }
}
