package by.geron.scanner.service.fileObject;

import by.geron.scanner.entity.FileObject;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface FileObjectService {

    FileObject findFileObject(String id);

    FileObject findFileObject(String name, String path);

    FileObject saveFileObject(FileObject fileObject);

    void deleteFileObject(String id);

    List<FileObject> findAllFileObjects(String idParent);

    boolean checkExistingFileObject(String name, String path);

    FileObject addFileObject(List<String> idFileObjects, File file, FileObject fileObject, List<String> extensions);

    FileObject buildFileObject(File file) throws IOException;

}
