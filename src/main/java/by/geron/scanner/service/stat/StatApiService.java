package by.geron.scanner.service.stat;

import by.geron.scanner.dto.request.ActingUserRequest;
import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.response.PathScanStatResponse;
import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.entity.Type;
import by.geron.scanner.mapper.pathScanStatResponse.PathScanStatResponseMapper;
import by.geron.scanner.service.businessLog.BusinessLogService;
import by.geron.scanner.service.scan.DB.ScanDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatApiService implements StatService {

    private final ScanDBService scanDBService;

    private final BusinessLogService businessLogService;

    private final PathScanStatResponseMapper pathScanStatResponseMapper;

    @Override
    public PathScanStatResponse getPathScanStat(PathRequest request) throws IOException {
        Map<Type, Integer> typeStat = new HashMap<>();
        Map<String, Integer> extensionStat = new HashMap<>();
        List<FileObject> fileObjects = scanDBService.scanDb(request.getPath());
        fileObjects.stream()
                .map(FileObject::getType)
                .forEach(type -> typeStat.merge(type, 1, Integer::sum));
        fileObjects.stream()
                .filter(fileObject -> fileObject.getType().equals(Type.FILE))
                .map(FileObject::getExtension)
                .filter(Objects::nonNull)
                .forEach(extension -> extensionStat.merge(extension, 1, Integer::sum));
        countFilesWithEmptyExtension(typeStat, extensionStat);
        return pathScanStatResponseMapper.mapsToPathScanStatResponseMapper(typeStat, extensionStat);
    }

    @Override
    public List<BusinessLog> getActingUserStat(ActingUserRequest request) {
        return businessLogService.findAllBusinessLog(request.getStartLogDateTime(), request.getFinishLogDateTime());
    }

    private void countFilesWithEmptyExtension(Map<Type, Integer> typeStat, Map<String, Integer> extensionStat) {
        int sumFiles = extensionStat.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        if (typeStat.get(Type.FILE) > sumFiles) {
            extensionStat.put("", typeStat.get(Type.FILE) - sumFiles);
        }
    }
}
