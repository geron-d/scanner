package by.geron.scanner.service;

import by.geron.scanner.dto.request.ScanRequest;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.entity.Type;
import by.geron.scanner.exceptions.EntityExistsException;
import by.geron.scanner.repository.FileObjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileObjectApiService implements FileObjectService {

    private final FileObjectRepository fileObjectRepository;

    public List<FileObject> scan(ScanRequest request) throws IOException {
        List<FileObject> fileObjects = new ArrayList<>();
        File file = buildFile(request.getPath());
        FileObject fileObject = buildFileObject(file);
        checkExistingFileObject(fileObject);
        addFileObject(fileObjects, file, fileObject, request.getExtensions());
        if (file.isDirectory()) {
            doChildScan(fileObjects, file, request.getExtensions());
        }
        return fileObjects;
    }

    private void doChildScan(List<FileObject> fileObjects, File file, List<String> extensions) throws IOException {
        File[] files = file.listFiles();
        for (File value : Objects.requireNonNull(files)) {
            fileObjects.addAll(scan(
                    ScanRequest.builder()
                            .path(value.getPath())
                            .extensions(extensions)
                            .build()));
        }
    }

    private void addFileObject(List<FileObject> fileObjects, File file, FileObject fileObject, List<String> extensions) {
        if (file.isDirectory()) {
            fileObjects.add(fileObjectRepository.save(fileObject));
        } else if (isIgnoreExtension(fileObject.getName(), extensions)) {
            setFileObjectExtensionAndTypeFile(fileObject);
            fileObjects.add(fileObjectRepository.save(fileObject));
        }
    }

    private void setFileObjectExtensionAndTypeFile(FileObject fileObject) {
        int lastDot = fileObject.getName().lastIndexOf(".");
        String extension = fileObject.getName().substring(lastDot);
        fileObject.setExtension(extension);
        fileObject.setType(Type.FILE);
    }

    private boolean isIgnoreExtension(String name, List<String> extensions) {
        if (Objects.isNull(extensions) || extensions.isEmpty()) {
            return true;
        }
        Optional<String> extension = extensions.stream().filter(name::contains).findFirst();
        return extension.isPresent();
    }

    private void checkExistingFileObject(FileObject fileObject) {
        if (fileObjectRepository.existsByNameAndPath(fileObject.getName(), fileObject.getPath())) {
            throw new EntityExistsException("entity exists");
        }
    }

    private void setIdParent(File file, FileObject fileObject) {
        FileObject parent = getParentFileObject(file);
        if (Objects.nonNull(parent)) {
            fileObject.setIdParent(parent.getId());
        }
    }

    private FileObject getParentFileObject(File file) {
        File parent = new File(file.getParent());
        return fileObjectRepository.findByNameAndPath(parent.getName(), parent.getPath()).orElse(null);
    }

    private FileObject buildFileObject(File file) throws IOException {
        BasicFileAttributes attributes = getBasicFileAttributes(file);
        FileObject fileObject = FileObject.builder()
                .path(file.getPath())
                .name(file.getName())
                .extension(null)
                .type(Type.FOLDER)
                .creationTime(getCreationTime(attributes))
                .updatedTime(getUpdatedTime(attributes))
                .build();
        setIdParent(file, fileObject);
        return fileObject;
    }

    private LocalDateTime getUpdatedTime(BasicFileAttributes attributes) {
        return LocalDateTime.ofInstant(attributes.lastModifiedTime().toInstant(), ZoneId.systemDefault());
    }

    private LocalDateTime getCreationTime(BasicFileAttributes attributes) {
        return LocalDateTime.ofInstant(attributes.creationTime().toInstant(), ZoneId.systemDefault());
    }

    private BasicFileAttributes getBasicFileAttributes(File file) throws IOException {
        return Files.readAttributes(file.toPath(), BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
    }

    private File buildFile(String path) {
        return new File(path);
    }
}
