package by.geron.scanner.service.scan.FS;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.request.ScanRequest;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.mapper.file.FileMapper;
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

    private final FileMapper fileMapper;

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

    private List<String> scanFS(ScanRequest request) throws IOException {
        List<String> idFileObjects = new ArrayList<>();
        File file = fileMapper.pathToFile(request.getPath());
        FileObject fileObject = fileObjectService.buildFileObject(file);
        scanFS(idFileObjects, file, fileObject, request.getExtensions());
        if (file.isDirectory()) {
            doChildScanFS(idFileObjects, file, request.getExtensions());
        }
        return idFileObjects;
    }

    private void scanFS(List<String> idFileObjects, File file, FileObject fileObject, List<String> extensions) {
        if (!fileObjectService.checkExistingFileObjectByNameIdParentAndCreationTime(fileObject.getName(),
                fileObject.getIdParent(), fileObject.getCreationTime().withNano(0))) {
            if (fileObjectService.checkExistingFileObjectByIdParentAndCreationTime(
                    fileObject.getIdParent(), fileObject.getCreationTime())) {
                saveRenamedFileObject(idFileObjects, file, fileObject, extensions);
            } else {
                saveCreatedFileObject(idFileObjects, file, fileObject, extensions);
            }
        } else {
            FileObject fileObjectDb = fileObjectService.findFileObjectByNameAndCreationTime(fileObject.getName(),
                    fileObject.getCreationTime().withNano(0));
            if (fileObject.getName().equals(fileObjectDb.getName())
                    && fileObject.getCreationTime().withNano(0).equals(fileObjectDb.getCreationTime())
                    && fileObject.getUpdatedTime().withNano(0).equals(fileObjectDb.getUpdatedTime())) {
                fileObjectService.addFileObject(idFileObjects, file, fileObjectDb, extensions);
            } else {
                saveUpdatedFileObject(idFileObjects, file, fileObject, fileObjectDb, extensions);
            }
        }
    }

    private void saveRenamedFileObject(List<String> idFileObjects, File file, FileObject fileObject,
                                       List<String> extensions) {
        FileObject fileObjectDb = fileObjectService.findFileObjectByCreationTime(fileObject.getCreationTime()
                .withNano(0));
        businessLogService.saveRenamedBusinessLog(fileObject, fileObjectDb);
        fileObject.setId(fileObjectDb.getId());
        fileObjectService.addFileObject(idFileObjects, file, fileObject, extensions);
    }

    private void saveCreatedFileObject(List<String> idFileObjects, File file, FileObject fileObject,
                                       List<String> extensions) {
        fileObject = fileObjectService.addFileObject(idFileObjects, file, fileObject, extensions);
        businessLogService.saveCreatedBusinessLog(fileObject);
    }

    private void saveUpdatedFileObject(List<String> idFileObjects, File file, FileObject fileObject,
                                       FileObject fileObjectDb, List<String> extensions) {
        businessLogService.saveUpdatedBusinessLog(fileObject, fileObjectDb);
        fileObject.setId(fileObjectDb.getId());
        doParenUpdated(fileObject);
        fileObjectService.addFileObject(idFileObjects, file, fileObject, extensions);
    }

    private void doParenUpdated(FileObject fileObject) {
        if (Objects.nonNull(fileObject.getIdParent())) {
            FileObject parent = fileObjectService.findFileObject(fileObject.getIdParent());
            businessLogService.saveUpdatedBusinessLog(parent, parent);
            doParenUpdated(parent);
        }
    }

    private void doChildScanFS(List<String> idFileObjects, File file, List<String> extensions) throws IOException {
        File[] files = file.listFiles();
        for (File value : Objects.requireNonNull(files)) {
            idFileObjects.addAll(scanFS(scanRequestMapper
                    .pathAndExtensionsToScanRequest(value.getPath(), extensions)));
        }
    }

}
