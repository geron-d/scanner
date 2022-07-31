package by.geron.scanner.service.scan.DB;

import by.geron.scanner.dto.request.ScanRequest;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.entity.Type;
import by.geron.scanner.mapper.file.FileMapper;
import by.geron.scanner.mapper.scanRequest.ScanRequestMapper;
import by.geron.scanner.service.fileObject.FileObjectService;
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
public class ScanDBApiService implements ScanDBService {

    private final FileObjectService fileObjectService;

    private final ScanRequestMapper scanRequestMapper;

    private final FileMapper fileMapper;

    @Override
    public List<String> scanDb(ScanRequest request) throws IOException {
        List<String> idFileObjects = new ArrayList<>();
        File file = fileMapper.pathToFile(request.getPath());
        if (fileObjectService.checkExistingFileObject(file.getName(), file.getPath())) {
            FileObject fileObject = fileObjectService.findFileObject(file.getName(), file.getPath());
            idFileObjects.add(fileObject.getId());
            if (fileObject.getType().equals(Type.FOLDER)) {
                doChildScanDb(idFileObjects, fileObject, request.getExtensions());
            }
        }
        return idFileObjects;
    }

    @Override
    public List<FileObject> scanDb(String path) throws IOException {
        List<FileObject> fileObjects = new ArrayList<>();
        File file = fileMapper.pathToFile(path);
        if (fileObjectService.checkExistingFileObject(file.getName(), file.getPath())) {
            FileObject fileObject = fileObjectService.findFileObject(file.getName(), file.getPath());
            fileObjects.add(fileObject);
            if (fileObject.getType().equals(Type.FOLDER)) {
                doChildScanDb(fileObjects, fileObject);
            }
        }
        return fileObjects;
    }

    private void doChildScanDb(List<String> idFileObjects, FileObject fileObject, List<String> extensions) throws IOException {
        List<FileObject> childFileObjects = fileObjectService.findAllFileObjects(fileObject.getId());
        for (FileObject value : childFileObjects) {
            idFileObjects.addAll(scanDb(scanRequestMapper
                    .pathAndExtensionsToScanRequest(value.getPath(), extensions)));
        }
    }

    private void doChildScanDb(List<FileObject> fileObjects, FileObject fileObject) throws IOException {
        List<FileObject> childFileObjects = fileObjectService.findAllFileObjects(fileObject.getId());
        for (FileObject value : childFileObjects) {
            fileObjects.addAll(scanDb(value.getPath()));
        }
    }

}
