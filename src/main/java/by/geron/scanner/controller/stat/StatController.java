package by.geron.scanner.controller.stat;

import by.geron.scanner.dto.request.ActingUserRequest;
import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.response.PathScanStatResponse;
import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.service.stat.StatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/stat")
@RequiredArgsConstructor
public class StatController {

    private final StatService statService;

    @GetMapping("scan")
    public PathScanStatResponse scan(@RequestBody @Valid PathRequest request) throws IOException {
        return statService.getPathScanStat(request);
    }

    @GetMapping("userActing")
    public List<BusinessLog> checkUserActing(@RequestBody @Valid ActingUserRequest request) {
        return statService.getActingUserStat(request);
    }
}
