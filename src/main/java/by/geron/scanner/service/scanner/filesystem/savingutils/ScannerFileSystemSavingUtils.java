package by.geron.scanner.service.scanner.filesystem.savingutils;

import by.geron.scanner.entity.FileObject;

import java.io.File;
import java.util.List;

public interface ScannerFileSystemSavingUtils {

    void saveRenamedFileObject(List<String> idFileObjects, File file, FileObject fileObject,
                               List<String> extensions);

    void saveCreatedFileObject(List<String> idFileObjects, File file, FileObject fileObject,
                               List<String> extensions);

    void saveUpdatedFileObject(List<String> idFileObjects, File file, FileObject fileObject,
                               FileObject fileObjectDb, List<String> extensions);

    void doParenUpdated(FileObject fileObject);

    FileObject addFileObject(List<String> fileObjects, File file, FileObject fileObject, List<String> extensions);

}
