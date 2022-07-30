package by.geron.scanner.service.stat;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.response.PathScanStatResponse;

import java.io.IOException;

public interface StatService {

    PathScanStatResponse getPathScanStat(PathRequest request) throws IOException;

}
