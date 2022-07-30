package by.geron.scanner.service.scan.FS;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.request.ScanRequest;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.mapper.scanRequest.ScanRequestMapper;
import by.geron.scanner.service.businessLog.BusinessLogService;
import by.geron.scanner.service.fileObject.FileObjectService;
import by.geron.scanner.service.scan.DB.ScanDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScanFSApiService implements ScanFSService {

    private final FileObjectService fileObjectService;

    private final BusinessLogService businessLogService;

    private final ScanRequestMapper scanRequestMapper;

    private final ScanDBService scanDBService;

    @Override
    public List<String> scan(ScanRequest request) throws IOException {
        List<String> FileObjectsDB = scanDBService.scanDb(request);
        List<String> fileObjectsFS = scanFS(request);
        FileObjectsDB.removeAll(fileObjectsFS);
        FileObjectsDB.forEach(id -> {
            businessLogService.saveDeletedBusinessLog(fileObjectService.findFileObject(id));
            fileObjectService.deleteFileObject(id);
        });
        return fileObjectsFS;
    }

    @Override
    public List<String> scan(PathRequest request) throws IOException {
        return scan(scanRequestMapper.pathRequestToScanRequest(request));
    }

    public List<String> scanFS(ScanRequest request) throws IOException {
        List<String> idFileObjects = new ArrayList<>();
        File file = buildFile(request.getPath());
        FileObject fileObject = fileObjectService.buildFileObject(file);
        if (!fileObjectService.checkExistingFileObject(fileObject.getName(), fileObject.getPath())) {
            fileObject = fileObjectService.addFileObject(idFileObjects, file, fileObject, request.getExtensions());
            businessLogService.saveCreatedBusinessLog(fileObject);
        } else {
            FileObject fileObjectDb = fileObjectService.findFileObject(fileObject.getName(), fileObject.getPath());
            if (fileObject.getName().equals(fileObjectDb.getName())
                    && fileObject.getPath().equals(fileObjectDb.getPath())
                    && fileObject.getUpdatedTime().equals(fileObjectDb.getUpdatedTime())) {
                fileObjectService.addFileObject(idFileObjects, file, fileObjectDb, request.getExtensions());
            } else {
                businessLogService.saveUpdatedBusinessLog(fileObject, fileObjectDb);
                fileObject.setId(fileObjectDb.getId());
                fileObjectService.addFileObject(idFileObjects, file, fileObject, request.getExtensions());
            }
        }
        if (file.isDirectory()) {
            doChildScanFS(idFileObjects, file, request.getExtensions());
        }
        return idFileObjects;
    }

    private void doChildScanFS(List<String> idFileObjects, File file, List<String> extensions) throws IOException {
        File[] files = file.listFiles();
        for (File value : Objects.requireNonNull(files)) {
            idFileObjects.addAll(scanFS(scanRequestMapper
                    .pathAndExtensionsToScanRequest(value.getPath(), extensions)));
        }
    }

    private File buildFile(String path) {
        return new File(path);
    }
}
