package by.geron.scanner.service.stat;

import by.geron.scanner.dto.request.ActingUserAfterRequest;
import by.geron.scanner.dto.request.ActingUserBetweenRequest;
import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.response.PathScanStatResponse;
import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.entity.Type;
import by.geron.scanner.mapper.file.FileMapper;
import by.geron.scanner.mapper.pathScanStatResponse.PathScanStatResponseMapper;
import by.geron.scanner.service.businessLog.BusinessLogService;
import by.geron.scanner.service.fileAttributes.BasicFileAttributesService;
import by.geron.scanner.service.fileAttributes.DosFileAttributesService;
import by.geron.scanner.service.fileObject.FileObjectService;
import by.geron.scanner.service.scan.DB.ScanDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatApiService implements StatService {

    private final ScanDBService scanDBService;

    private final BusinessLogService businessLogService;

    private final FileObjectService fileObjectService;

    private final BasicFileAttributesService basicFileAttributesService;

    private final DosFileAttributesService dosFileAttributesService;

    private final PathScanStatResponseMapper pathScanStatResponseMapper;

    private final FileMapper fileMapper;

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
    public List<BusinessLog> getActingUserStat(ActingUserBetweenRequest request) {
        return businessLogService.findAllBusinessLog(request.getStartLogDateTime(), request.getFinishLogDateTime());
    }

    @Override
    public List<BusinessLog> getActingUserStat(ActingUserAfterRequest request) {
        return businessLogService.findAllBusinessLog(request.getStartLogDateTime());
    }

    @Override
    public LinkedHashMap<String, String> getPathStat(PathRequest request) throws IOException {
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
    public LinkedHashMap<String, String> getDbPathStat(PathRequest request) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        FileObject fileObject = fileObjectService.findFileObjectByPathRequest(request);
        if (Objects.equals(fileObject.getType(), Type.FOLDER)) {
            map.put("folder structure", fileObject.getPath());
        } else {
            map.putAll(fileObjectService.getDbFileAttributes(fileObject));
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
