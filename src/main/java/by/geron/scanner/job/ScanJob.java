package by.geron.scanner.job;

import by.geron.scanner.mapper.pathRequest.PathRequestMapper;
import by.geron.scanner.service.scan.FS.ScanFSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScanJob {

    private final ScanFSService scanFSService;

    private final PathRequestMapper pathRequestMapper;

    @Value("${job.scanJobPath}")
    private String path;

    @Scheduled(fixedRateString = "${job.scanFixedRate}")
    public void scanFS() throws IOException {
        scanFSService.scan(pathRequestMapper.pathToPathRequest(path));
    }
}
