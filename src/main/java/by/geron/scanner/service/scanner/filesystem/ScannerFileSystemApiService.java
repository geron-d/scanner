package by.geron.scanner.service.scanner.filesystem;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.request.ScannerRequest;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.mapper.file.FileMapper;
import by.geron.scanner.mapper.scannerrequest.ScannerRequestMapper;
import by.geron.scanner.service.businesslog.BusinessLogService;
import by.geron.scanner.service.fileobject.FileObjectService;
import by.geron.scanner.service.scanner.database.ScannerDatabaseService;
import by.geron.scanner.service.scanner.filesystem.savingutils.ScannerFileSystemSavingUtils;
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
public class ScannerFileSystemApiService implements ScannerFileSystemService {

    private final FileObjectService fileObjectService;

    private final BusinessLogService businessLogService;

    private final ScannerRequestMapper scannerRequestMapper;

    private final FileMapper fileMapper;

    private final ScannerDatabaseService scannerDatabaseService;

    private final ScannerFileSystemSavingUtils scannerFileSystemSavingUtils;

    @Override
    public List<String> scan(ScannerRequest request) throws IOException {
        List<String> FileObjectsDB = scannerDatabaseService.scanDatabase(request);
        List<String> fileObjectsFS = scanFileSystem(request);
        FileObjectsDB.removeAll(fileObjectsFS);
        FileObjectsDB.forEach(id -> {
            businessLogService.saveDeletedBusinessLog(fileObjectService.findFileObject(id));
            fileObjectService.deleteFileObject(id);
        });
        return fileObjectsFS;
    }

    @Override
    public List<String> scan(PathRequest request) throws IOException {
        return scan(scannerRequestMapper.pathRequestToScanRequest(request));
    }

    private List<String> scanFileSystem(ScannerRequest request) throws IOException {
        List<String> idFileObjects = new ArrayList<>();
        File file = fileMapper.pathToFile(request.getPath());
        FileObject fileObject = fileObjectService.buildFileObject(file);
        scanFileSystem(idFileObjects, file, fileObject, request.getExtensions());
        if (file.isDirectory()) {
            doChildScanFileSystem(idFileObjects, file, request.getExtensions());
        }
        return idFileObjects;
    }

    private void scanFileSystem(List<String> idFileObjects, File file, FileObject fileObject, List<String> extensions) {
        if (!fileObjectService.checkExistingFileObjectByNameIdParentAndCreationTime(fileObject.getName(),
                fileObject.getIdParent(), fileObject.getCreationTime().withNano(0))) {
            if (fileObjectService.checkExistingFileObjectByIdParentAndCreationTime(
                    fileObject.getIdParent(), fileObject.getCreationTime())) {
                scannerFileSystemSavingUtils.saveRenamedFileObject(idFileObjects, file, fileObject, extensions);
            } else {
                scannerFileSystemSavingUtils.saveCreatedFileObject(idFileObjects, file, fileObject, extensions);
            }
        } else {
            FileObject fileObjectDb = fileObjectService.findFileObjectByNameAndCreationTime(fileObject.getName(),
                    fileObject.getCreationTime().withNano(0));
            if (fileObject.getName().equals(fileObjectDb.getName())
                    && fileObject.getCreationTime().withNano(0).equals(fileObjectDb.getCreationTime())
                    && fileObject.getUpdatedTime().withNano(0).equals(fileObjectDb.getUpdatedTime())) {
                scannerFileSystemSavingUtils.addFileObject(idFileObjects, file, fileObjectDb, extensions);
            } else {
                scannerFileSystemSavingUtils
                        .saveUpdatedFileObject(idFileObjects, file, fileObject, fileObjectDb, extensions);
            }
        }
    }

    private void doChildScanFileSystem(List<String> idFileObjects, File file,
                                       List<String> extensions) throws IOException {
        File[] files = file.listFiles();
        for (File value : Objects.requireNonNull(files)) {
            idFileObjects.addAll(scanFileSystem(scannerRequestMapper
                    .pathAndExtensionsToScanRequest(value.getPath(), extensions)));
        }
    }

}
