package by.geron.scanner.service.scanner.database;

import by.geron.scanner.dto.request.ScannerRequest;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.entity.Type;
import by.geron.scanner.mapper.file.FileMapper;
import by.geron.scanner.mapper.scannerrequest.ScannerRequestMapper;
import by.geron.scanner.service.fileobject.FileObjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScannerDatabaseApiService implements ScannerDatabaseService {

    private final FileObjectService fileObjectService;

    private final ScannerRequestMapper scannerRequestMapper;

    private final FileMapper fileMapper;

    @Override
    public List<String> scanDatabase(ScannerRequest request) throws IOException {
        List<FileObject> fileObjects = scanDatabaseToObject(request);
        return fileObjects.stream()
                .map(FileObject::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<FileObject> scanDatabase(String path) throws IOException {
        ScannerRequest scannerRequest = scannerRequestMapper.pathToScanRequest(path);
        return scanDatabaseToObject(scannerRequest);
    }

    private List<FileObject> scanDatabaseToObject(ScannerRequest request) throws IOException {
        List<FileObject> idFileObjects = new ArrayList<>();
        File file = fileMapper.pathToFile(request.getPath());
        if (fileObjectService.checkExistingFileObject(file.getName(), file.getPath())) {
            FileObject fileObject = fileObjectService.findFileObject(file.getName(), file.getPath());
            idFileObjects.add(fileObject);
            if (fileObject.getType().equals(Type.FOLDER)) {
                doChildScanDatabase(idFileObjects, fileObject, request.getExtensions());
            }
        }
        return idFileObjects;
    }

    private void doChildScanDatabase(List<FileObject> idFileObjects, FileObject fileObject, List<String> extensions) throws IOException {
        List<FileObject> childFileObjects = fileObjectService.findAllFileObjects(fileObject.getId());
        for (FileObject value : childFileObjects) {
            idFileObjects.addAll(scanDatabaseToObject(scannerRequestMapper
                    .pathAndExtensionsToScanRequest(value.getPath(), extensions)));
        }
    }

}
