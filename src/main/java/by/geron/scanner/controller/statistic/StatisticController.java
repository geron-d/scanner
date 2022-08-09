package by.geron.scanner.controller.statistic;

import by.geron.scanner.dto.request.ActingUserAfterRequest;
import by.geron.scanner.dto.request.ActingUserBetweenRequest;
import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.response.PathScannerStatisticResponse;
import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.service.statistic.StatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("scanner")
    public PathScannerStatisticResponse scan(@RequestBody @Valid PathRequest request) throws IOException {
        return statisticService.getPathScannerStatistic(request);
    }

    @GetMapping("user/between")
    public List<BusinessLog> checkUserActing(@RequestBody @Valid ActingUserBetweenRequest request,
                                             @PageableDefault(size = Integer.MAX_VALUE)
                                             @SortDefault(sort = "logDateTime") Pageable pageable) {
        return statisticService.getActingUserStatistic(request, pageable);
    }

    @GetMapping("user/after")
    public List<BusinessLog> checkUserActing(@RequestBody @Valid ActingUserAfterRequest request,
                                             @PageableDefault(size = Integer.MAX_VALUE)
                                             @SortDefault(sort = "logDateTime") Pageable pageable) {
        return statisticService.getActingUserStatistic(request, pageable);
    }

    @GetMapping("path")
    public LinkedHashMap<String, String> getPathStatistic(@RequestBody @Valid PathRequest request) throws IOException {
        return statisticService.getPathStatistic(request);
    }

    @GetMapping("path/database")
    public LinkedHashMap<String, String> getDatabasePathStatistic(@RequestBody @Valid PathRequest request) {
        return statisticService.getDatabasePathStatistic(request);
    }
}
