package by.geron.scanner.service.stat;

import by.geron.scanner.dto.request.ActingUserAfterRequest;
import by.geron.scanner.dto.request.ActingUserBetweenRequest;
import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.response.PathScanStatResponse;
import by.geron.scanner.entity.BusinessLog;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

public interface StatService {

    PathScanStatResponse getPathScanStat(PathRequest request) throws IOException;

    List<BusinessLog> getActingUserStat(ActingUserBetweenRequest request);

    List<BusinessLog> getActingUserStat(ActingUserAfterRequest request);

    LinkedHashMap<String, String> getPathStat(PathRequest request) throws IOException;

}
