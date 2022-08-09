package by.geron.scanner.service.scanner.filesystem.savingutils;

import by.geron.scanner.entity.FileObject;
import by.geron.scanner.service.businesslog.BusinessLogService;
import by.geron.scanner.service.fileobject.FileObjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScannerFileSystemSavingUtilsApi implements ScannerFileSystemSavingUtils {

    private final FileObjectService fileObjectService;

    private final BusinessLogService businessLogService;

    @Override
    public void saveRenamedFileObject(List<String> idFileObjects, File file, FileObject fileObject,
                                      List<String> extensions) {
        FileObject fileObjectDb = fileObjectService.findFileObjectByCreationTime(fileObject.getCreationTime()
                .withNano(0));
        businessLogService.saveRenamedBusinessLog(fileObject, fileObjectDb);
        fileObject.setId(fileObjectDb.getId());
        addFileObject(idFileObjects, file, fileObject, extensions);
    }

    @Override
    public void saveCreatedFileObject(List<String> idFileObjects, File file, FileObject fileObject,
                                      List<String> extensions) {
        fileObject = addFileObject(idFileObjects, file, fileObject, extensions);
        businessLogService.saveCreatedBusinessLog(fileObject);
    }

    @Override
    public void saveUpdatedFileObject(List<String> idFileObjects, File file, FileObject fileObject,
                                      FileObject fileObjectDb, List<String> extensions) {
        businessLogService.saveUpdatedBusinessLog(fileObject, fileObjectDb);
        fileObject.setId(fileObjectDb.getId());
        doParenUpdated(fileObject);
        addFileObject(idFileObjects, file, fileObject, extensions);
    }

    @Override
    public void doParenUpdated(FileObject fileObject) {
        if (Objects.nonNull(fileObject.getIdParent())) {
            FileObject parent = fileObjectService.findFileObject(fileObject.getIdParent());
            businessLogService.saveUpdatedBusinessLog(parent, parent);
            doParenUpdated(parent);
        }
    }

    @Override
    public FileObject addFileObject(List<String> fileObjects, File file, FileObject fileObject,
                                    List<String> extensions) {
        if (file.isDirectory()) {
            fileObject = fileObjectService.saveFileObject(fileObject);
            fileObjects.add(fileObject.getId());
        } else if (isIgnoreExtension(fileObject.getName(), extensions)) {
            fileObjectService.setFileObjectExtensionAndTypeFile(fileObject);
            fileObject = fileObjectService.saveFileObject(fileObject);
            fileObjects.add(fileObject.getId());
        }
        return fileObject;
    }

    private boolean isIgnoreExtension(String name, List<String> extensions) {
        if (Objects.isNull(extensions) || extensions.isEmpty()) {
            return true;
        }
        Optional<String> extension = extensions.stream().filter(name::contains).findFirst();
        return extension.isEmpty();
    }
}
