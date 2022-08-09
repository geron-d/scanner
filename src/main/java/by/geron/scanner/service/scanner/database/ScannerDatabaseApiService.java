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

@Slf4j
@Service
@RequiredArgsConstructor
public class ScannerDatabaseApiService implements ScannerDatabaseService {

    private final FileObjectService fileObjectService;

    private final ScannerRequestMapper scannerRequestMapper;

    private final FileMapper fileMapper;

    @Override
    public List<String> scanDatabase(ScannerRequest request) throws IOException {
        List<String> idFileObjects = new ArrayList<>();
        File file = fileMapper.pathToFile(request.getPath());
        if (fileObjectService.checkExistingFileObject(file.getName(), file.getPath())) {
            FileObject fileObject = fileObjectService.findFileObject(file.getName(), file.getPath());
            idFileObjects.add(fileObject.getId());
            if (fileObject.getType().equals(Type.FOLDER)) {
                doChildScanDatabase(idFileObjects, fileObject, request.getExtensions());
            }
        }
        return idFileObjects;
    }

    @Override
    public List<FileObject> scanDatabase(String path) throws IOException {
        List<FileObject> fileObjects = new ArrayList<>();
        File file = fileMapper.pathToFile(path);
        if (fileObjectService.checkExistingFileObject(file.getName(), file.getPath())) {
            FileObject fileObject = fileObjectService.findFileObject(file.getName(), file.getPath());
            fileObjects.add(fileObject);
            if (fileObject.getType().equals(Type.FOLDER)) {
                doChildScanDatabase(fileObjects, fileObject);
            }
        }
        return fileObjects;
    }

    private void doChildScanDatabase(List<String> idFileObjects, FileObject fileObject, List<String> extensions) throws IOException {
        List<FileObject> childFileObjects = fileObjectService.findAllFileObjects(fileObject.getId());
        for (FileObject value : childFileObjects) {
            idFileObjects.addAll(scanDatabase(scannerRequestMapper
                    .pathAndExtensionsToScanRequest(value.getPath(), extensions)));
        }
    }

    private void doChildScanDatabase(List<FileObject> fileObjects, FileObject fileObject) throws IOException {
        List<FileObject> childFileObjects = fileObjectService.findAllFileObjects(fileObject.getId());
        for (FileObject value : childFileObjects) {
            fileObjects.addAll(scanDatabase(value.getPath()));
        }
    }

}
