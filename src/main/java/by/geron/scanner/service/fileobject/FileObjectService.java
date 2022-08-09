package by.geron.scanner.service.fileobject;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.entity.FileObject;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

public interface FileObjectService {

    FileObject findFileObject(String id);

    FileObject findFileObject(String name, String path);

    FileObject findFileObjectByNameAndCreationTime(String name, LocalDateTime creationTime);

    FileObject findFileObjectByCreationTime(LocalDateTime creationTime);

    FileObject findFileObjectByPathRequest(PathRequest request);

    FileObject saveFileObject(FileObject fileObject);

    void deleteFileObject(String id);

    List<FileObject> findAllFileObjects(String idParent);

    boolean checkExistingFileObject(String name, String path);

    boolean checkExistingFileObjectByNameIdParentAndCreationTime(String name, String idParent, LocalDateTime creationTime);

    boolean checkExistingFileObjectByIdParentAndCreationTime(String idParent, LocalDateTime creationTime);

    FileObject addFileObject(List<String> idFileObjects, File file, FileObject fileObject, List<String> extensions);

    FileObject buildFileObject(File file) throws IOException;

    LinkedHashMap<String, String> getDatabaseFileAttributes(FileObject fileObject);

}
