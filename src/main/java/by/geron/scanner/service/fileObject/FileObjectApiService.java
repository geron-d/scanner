package by.geron.scanner.service.fileObject;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.request.ScanRequest;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.entity.Type;
import by.geron.scanner.repository.fileObject.FileObjectRepository;
import by.geron.scanner.service.businessLog.BusinessLogService;
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
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileObjectApiService implements FileObjectService {

    private final FileObjectRepository fileObjectRepository;

    private final BusinessLogService businessLogService;

    @Override
    public List<FileObject> scan(ScanRequest request) throws IOException {
        List<FileObject> fileObjectsFileSystem = scanFileSystem(request);
        List<FileObject> childFileObjectsDb = dbScanChild(fileObjectsFileSystem);
        fileObjectsFileSystem.forEach(childFileObjectsDb::remove);
        childFileObjectsDb.forEach(fileObject -> {
            businessLogService.saveDeletedBusinessLog(fileObject);
            fileObjectRepository.delete(fileObject);
        });
        return fileObjectsFileSystem;
    }

    @Override
    public List<FileObject> scan(PathRequest request) throws IOException {
        return scan(buildScanRequest(request));
    }

    public List<FileObject> scanFileSystem(ScanRequest request) throws IOException {
        List<FileObject> fileObjects = new ArrayList<>();
        File file = buildFile(request.getPath());
        FileObject fileObject = buildFileObject(file);
        if (!checkExistingFileObject(fileObject)) {
            fileObject = addFileObject(fileObjects, file, fileObject, request.getExtensions());
            businessLogService.saveCreatedBusinessLog(fileObject);
        } else {
            FileObject fileObjectDb = fileObjectRepository.findByNameAndPath(fileObject.getName(), fileObject.getPath())
                    .orElseThrow(NoSuchElementException::new);
            if (fileObject.getName().equals(fileObjectDb.getName())
                    && fileObject.getPath().equals(fileObjectDb.getPath())
                    && fileObject.getUpdatedTime().equals(fileObjectDb.getUpdatedTime())) {
                addFileObject(fileObjects, file, fileObjectDb, request.getExtensions());
            } else {
                businessLogService.saveUpdatedBusinessLog(fileObject, fileObjectDb);
                fileObject.setId(fileObjectDb.getId());
                addFileObject(fileObjects, file, fileObject, request.getExtensions());
            }
        }
        if (file.isDirectory()) {
            doChildScan(fileObjects, file, request.getExtensions());
        }
        return fileObjects;
    }

    private List<FileObject> dbScanChild(List<FileObject> fileObjects) {
        List<FileObject> childFileObjectsDb = new ArrayList<>();
        fileObjects.forEach(fileObject -> childFileObjectsDb.addAll(fileObjectRepository
                .findAllByIdParent(fileObject.getId())));
        return childFileObjectsDb;
    }

    private void doChildScan(List<FileObject> fileObjects, File file, List<String> extensions) throws IOException {
        File[] files = file.listFiles();
        for (File value : Objects.requireNonNull(files)) {
            fileObjects.addAll(scanFileSystem(buildScanRequest(value.getPath(), extensions)));
        }
    }

    private FileObject addFileObject(List<FileObject> fileObjects, File file, FileObject fileObject, List<String> extensions) {
        if (file.isDirectory()) {
            fileObject = fileObjectRepository.save(fileObject);
            fileObjects.add(fileObject);
        } else if (isIgnoreExtension(fileObject.getName(), extensions)) {
            setFileObjectExtensionAndTypeFile(fileObject);
            fileObject = fileObjectRepository.save(fileObject);
            fileObjects.add(fileObject);
        }
        return fileObject;
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

    private boolean checkExistingFileObject(FileObject fileObject) {
        return fileObjectRepository.existsByNameAndPath(fileObject.getName(), fileObject.getPath());
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
        return LocalDateTime.ofInstant(attributes.lastModifiedTime().toInstant(),
                ZoneId.systemDefault()).withNano(0);
    }

    private LocalDateTime getCreationTime(BasicFileAttributes attributes) {
        return LocalDateTime.ofInstant(attributes.creationTime().toInstant(),
                ZoneId.systemDefault()).withNano(0);
    }

    private BasicFileAttributes getBasicFileAttributes(File file) throws IOException {
        return Files.readAttributes(file.toPath(), BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
    }

    private ScanRequest buildScanRequest(PathRequest request) {
        return ScanRequest.builder()
                .path(request.getPath())
                .extensions(List.of())
                .build();
    }

    private ScanRequest buildScanRequest(String path, List<String> extensions) {
        return ScanRequest.builder()
                .path(path)
                .extensions(extensions)
                .build();
    }

    private File buildFile(String path) {
        return new File(path);
    }
}
