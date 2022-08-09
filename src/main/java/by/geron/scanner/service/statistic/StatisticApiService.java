package by.geron.scanner.service.statistic;

import by.geron.scanner.dto.request.ActingUserAfterRequest;
import by.geron.scanner.dto.request.ActingUserBetweenRequest;
import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.response.PathScannerStatisticResponse;
import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.entity.Type;
import by.geron.scanner.mapper.file.FileMapper;
import by.geron.scanner.mapper.pathscannerstatisticresponse.PathScannerStatisticResponseMapper;
import by.geron.scanner.service.businesslog.BusinessLogService;
import by.geron.scanner.service.fileattributes.BasicFileAttributesService;
import by.geron.scanner.service.fileattributes.DosFileAttributesService;
import by.geron.scanner.service.fileobject.FileObjectService;
import by.geron.scanner.service.scanner.database.ScannerDatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticApiService implements StatisticService {

    private final ScannerDatabaseService scannerDatabaseService;

    private final BusinessLogService businessLogService;

    private final FileObjectService fileObjectService;

    private final BasicFileAttributesService basicFileAttributesService;

    private final DosFileAttributesService dosFileAttributesService;

    private final PathScannerStatisticResponseMapper pathScannerStatisticResponseMapper;

    private final FileMapper fileMapper;

    @Override
    public PathScannerStatisticResponse getPathScannerStatistic(PathRequest request) throws IOException {
        Map<Type, Integer> typeStat = new HashMap<>();
        Map<String, Integer> extensionStat = new HashMap<>();
        List<FileObject> fileObjects = scannerDatabaseService.scanDatabase(request.getPath());
        fileObjects.stream()
                .map(FileObject::getType)
                .forEach(type -> typeStat.merge(type, 1, Integer::sum));
        fileObjects.stream()
                .filter(fileObject -> fileObject.getType().equals(Type.FILE))
                .map(FileObject::getExtension)
                .filter(Objects::nonNull)
                .forEach(extension -> extensionStat.merge(extension, 1, Integer::sum));
        countFilesWithEmptyExtension(typeStat, extensionStat);
        return pathScannerStatisticResponseMapper.mapsToPathScanStatResponseMapper(typeStat, extensionStat);
    }

    @Override
    public List<BusinessLog> getActingUserStatistic(ActingUserBetweenRequest request, Pageable pageable) {
        return businessLogService
                .findAllBusinessLog(request.getStartLogDateTime(), request.getFinishLogDateTime(), pageable);
    }

    @Override
    public List<BusinessLog> getActingUserStatistic(ActingUserAfterRequest request, Pageable pageable) {
        return businessLogService.findAllBusinessLog(request.getStartLogDateTime(), pageable);
    }

    @Override
    public LinkedHashMap<String, String> getPathStatistic(PathRequest request) throws IOException {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        File file = fileMapper.pathToFile(request.getPath());
        if (file.isDirectory()) {
            map.put("folder structure", file.getAbsolutePath());
        } else {
            map.put("file", String.valueOf(file.getAbsoluteFile()));
            map.putAll(basicFileAttributesService.getMapBasicFileAttributes(file));
            map.putAll(dosFileAttributesService.getMapDosFileAttributes(file));
        }
        return map;
    }

    @Override
    public LinkedHashMap<String, String> getDatabasePathStatistic(PathRequest request) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        FileObject fileObject = fileObjectService.findFileObjectByPathRequest(request);
        if (Objects.equals(fileObject.getType(), Type.FOLDER)) {
            map.put("folder structure", fileObject.getPath());
        } else {
            map.putAll(fileObjectService.getDatabaseFileAttributes(fileObject));
        }
        return map;
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
