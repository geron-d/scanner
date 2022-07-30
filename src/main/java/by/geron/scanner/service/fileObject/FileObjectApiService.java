package by.geron.scanner.service.fileObject;

import by.geron.scanner.dto.response.CreationAndUpdatedTimeResponse;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.entity.Type;
import by.geron.scanner.repository.fileObject.FileObjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileObjectApiService implements FileObjectService {

    private final FileObjectRepository fileObjectRepository;

    private final FileObjectAttributesService fileObjectAttributesService;

    @Override
    public FileObject findFileObject(String id) {
        return fileObjectRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public FileObject findFileObject(String name, String path) {
        return fileObjectRepository.findByNameAndPath(name, path).orElseThrow(NoSuchElementException::new);
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
        CreationAndUpdatedTimeResponse response = fileObjectAttributesService.getCreationAndUpdatedTime(file);
        return FileObject.builder()
                .idParent(getIdParent(file))
                .path(file.getPath())
                .name(file.getName())
                .type(Type.FOLDER)
                .creationTime(response.getCreationTime())
                .updatedTime(response.getUpdatedTime())
                .build();
    }

    private void setFileObjectExtensionAndTypeFile(FileObject fileObject) {
        int lastDot = fileObject.getName().lastIndexOf(".");
        if (lastDot > -1) {
            String extension = fileObject.getName().substring(lastDot);
            fileObject.setExtension(extension);
            fileObject.setType(Type.FILE);
        }
    }

    private boolean isIgnoreExtension(String name, List<String> extensions) {
        if (Objects.isNull(extensions) || extensions.isEmpty()) {
            return true;
        }
        Optional<String> extension = extensions.stream().filter(name::contains).findFirst();
        return extension.isPresent();
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
        }
        catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

}
