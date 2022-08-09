package by.geron.scanner.service.statistic;

import by.geron.scanner.dto.request.ActingUserAfterRequest;
import by.geron.scanner.dto.request.ActingUserBetweenRequest;
import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.response.PathScannerStatisticResponse;
import by.geron.scanner.entity.BusinessLog;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

public interface StatisticService {

    PathScannerStatisticResponse getPathScannerStatistic(PathRequest request) throws IOException;

    List<BusinessLog> getActingUserStatistic(ActingUserBetweenRequest request, Pageable pageable);

    List<BusinessLog> getActingUserStatistic(ActingUserAfterRequest request, Pageable pageable);

    LinkedHashMap<String, String> getPathStatistic(PathRequest request) throws IOException;

    LinkedHashMap<String, String> getDatabasePathStatistic(PathRequest request);
}
