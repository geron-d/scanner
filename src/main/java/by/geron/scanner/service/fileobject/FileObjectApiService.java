package by.geron.scanner.service.fileobject;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.response.CreationAndUpdatedTimeResponse;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.entity.Type;
import by.geron.scanner.mapper.file.FileMapper;
import by.geron.scanner.repository.fileobject.FileObjectRepository;
import by.geron.scanner.service.fileattributes.BasicFileAttributesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileObjectApiService implements FileObjectService {

    private final FileObjectRepository fileObjectRepository;

    private final BasicFileAttributesService basicFileAttributesService;

    private final FileMapper fileMapper;

    @Override
    public FileObject findFileObject(String id) {
        return fileObjectRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public FileObject findFileObject(String name, String path) {
        return fileObjectRepository.findByNameAndPath(name, path).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public FileObject findFileObjectByNameAndCreationTime(String name, LocalDateTime creationTime) {
        return fileObjectRepository.findByNameAndCreationTime(name, creationTime)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public FileObject findFileObjectByCreationTime(LocalDateTime creationTime) {
        return fileObjectRepository.findByCreationTime(creationTime)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public FileObject findFileObjectByPathRequest(PathRequest request) {
        File file = fileMapper.pathToFile(request.getPath());
        return fileObjectRepository.findByNameAndPath(file.getName(), file.getPath())
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public FileObject saveFileObject(FileObject fileObject) {
        return fileObjectRepository.save(fileObject);
    }

    @Override
    public void deleteFileObject(String id) {
        fileObjectRepository.deleteById(id);
    }

    @Override
    public List<FileObject> findAllFileObjects(String idParent) {
        return fileObjectRepository
                .findAllByIdParent(idParent);
    }

    @Override
    public boolean checkExistingFileObject(String name, String path) {
        return fileObjectRepository.existsByNameAndPath(name, path);
    }

    @Override
    public boolean checkExistingFileObjectByNameIdParentAndCreationTime(String name, String idParent,
                                                                        LocalDateTime creationTime) {
        return fileObjectRepository.existsByNameAndIdParentAndCreationTime(name, idParent, creationTime);
    }

    @Override
    public boolean checkExistingFileObjectByIdParentAndCreationTime(String idParent, LocalDateTime creationTime) {
        return fileObjectRepository.existsByIdParentAndCreationTime(idParent, creationTime);
    }

    @Override
    public FileObject addFileObject(List<String> fileObjects, File file, FileObject fileObject, List<String> extensions) {
        if (file.isDirectory()) {
            fileObject = saveFileObject(fileObject);
            fileObjects.add(fileObject.getId());
        } else if (isIgnoreExtension(fileObject.getName(), extensions)) {
            setFileObjectExtensionAndTypeFile(fileObject);
            fileObject = saveFileObject(fileObject);
            fileObjects.add(fileObject.getId());
        }
        return fileObject;
    }

    @Override
    public FileObject buildFileObject(File file) throws IOException {
        CreationAndUpdatedTimeResponse response = basicFileAttributesService.getCreationAndUpdatedTime(file);
        return FileObject.builder()
                .idParent(getIdParent(file))
                .path(file.getPath())
                .name(file.getName())
                .type(Type.FOLDER)
                .creationTime(response.getCreationTime())
                .updatedTime(response.getUpdatedTime())
                .build();
    }

    @Override
    public LinkedHashMap<String, String> getDatabaseFileAttributes(FileObject fileObject) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("file", fileObject.getPath());
        map.put("id", fileObject.getId());
        map.put("path", fileObject.getPath());
        map.put("name", fileObject.getName());
        map.put("idParent", fileObject.getIdParent());
        map.put("extension", fileObject.getExtension());
        map.put("creationTime", String.valueOf(fileObject.getCreationTime()));
        map.put("updatedTime", String.valueOf(fileObject.getUpdatedTime()));
        return map;
    }

    private void setFileObjectExtensionAndTypeFile(FileObject fileObject) {
        fileObject.setType(Type.FILE);
        int lastDot = fileObject.getName().lastIndexOf(".");
        if (lastDot > -1) {
            String extension = fileObject.getName().substring(lastDot);
            fileObject.setExtension(extension);
        }
    }

    private boolean isIgnoreExtension(String name, List<String> extensions) {
        if (Objects.isNull(extensions) || extensions.isEmpty()) {
            return true;
        }
        Optional<String> extension = extensions.stream().filter(name::contains).findFirst();
        return extension.isEmpty();
    }

    private String getIdParent(File file) {
        Optional<FileObject> parent = getParentFileObject(file);
        if (parent.isEmpty()) {
            return null;
        }
        return parent.map(FileObject::getId).orElse(null);
    }

    private Optional<FileObject> getParentFileObject(File file) {
        File parent = new File(file.getParent());
        try {
            return Optional.ofNullable(findFileObject(parent.getName(), parent.getPath()));
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

}
