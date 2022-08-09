package by.geron.scanner.service.fileobject;

import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.response.CreationAndUpdatedTimeResponse;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.entity.Type;
import by.geron.scanner.mapper.file.FileMapper;
import by.geron.scanner.repository.fileobject.FileObjectRepository;
import by.geron.scanner.service.fileattributes.BasicFileAttributesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing fileObject service")
public class FileObjectApiTest {

    @Mock
    private FileObjectRepository fileObjectRepository;

    @Mock
    private FileMapper fileMapper;

    @Mock
    private BasicFileAttributesService basicFileAttributesService;

    @InjectMocks
    private FileObjectApiService fileObjectApiService;

    private FileObject fileObjectFile;

    private final FileObject fileObjectFolder = setFileObjectFolder();

    @BeforeEach
    private void setFileObjectFile() {
        fileObjectFile = FileObject.builder()
                .id("62e7cd280c75de22fd3f15d7")
                .idParent("62e7cd280c75de22fd3f15d6")
                .path("resources\\1\\1.txt")
                .name("1.txt")
                .extension(".txt")
                .type(Type.FILE)
                .creationTime(LocalDateTime.of(2022, 7, 27, 8, 13, 29, 0))
                .updatedTime(LocalDateTime.of(2022, 7, 27, 8, 13, 29, 0))
                .build();
    }

    private FileObject setFileObjectFolder() {
        return FileObject.builder()
                .id("62e7cd280c75de22fd3f15d8")
                .idParent("62e7cd280c75de22fd3f15d6")
                .path("resources\\1\\2")
                .name("2")
                .extension(null)
                .type(Type.FOLDER)
                .creationTime(LocalDateTime.of(2022, 7, 27, 8, 13, 4, 0))
                .updatedTime(LocalDateTime.of(2022, 8, 1, 12, 55, 43, 0))
                .build();
    }

    private FileObject getFileObjectFileWithoutId() {
        return FileObject.builder()
                .idParent("62e7cd280c75de22fd3f15d6")
                .path("resources\\1\\1.txt")
                .name("1.txt")
                .extension(".txt")
                .type(Type.FILE)
                .creationTime(LocalDateTime.of(2022, 7, 27, 8, 13, 29, 0))
                .updatedTime(LocalDateTime.of(2022, 7, 27, 8, 13, 29, 0))
                .build();
    }

    private PathRequest getPathRequestFile() {
        return PathRequest.builder()
                .path("resources\\1\\1.txt")
                .build();
    }

    private File getFileFile() {
        return new File("resources\\1\\1.txt");
    }

    private List<FileObject> getFileObjectsList() {
        List<FileObject> fileObjects = new ArrayList<>();
        fileObjects.add(fileObjectFile);
        fileObjects.add(fileObjectFolder);
        return fileObjects;
    }

    private CreationAndUpdatedTimeResponse getCreationAndUpdatedTimeResponseFile() {
        return CreationAndUpdatedTimeResponse.builder()
                .creationTime(LocalDateTime.of(2022, 7, 27, 8, 13, 29, 0))
                .updatedTime(LocalDateTime.of(2022, 7, 27, 8, 13, 29, 0))
                .build();
    }

    @Test
    @DisplayName("JUnit test for findFileObject by id when fileObject exists for returning fileObject is not null")
    void checkFindFileObjectByIdWhenFileObjectExistsReturnsNotNull() {
        Mockito.when(fileObjectRepository.findById(fileObjectFile.getId()))
                .thenReturn(Optional.ofNullable(fileObjectFile));
        Assertions.assertNotNull(fileObjectApiService.findFileObject(fileObjectFile.getId()));
        Mockito.verify(fileObjectRepository, Mockito.times(1)).findById(fileObjectFile.getId());
    }

    @Test
    @DisplayName("JUnit test for findFileObject by id when fileObject exists")
    void checkFindFileObjectByIdWhenFileObjectExists() {
        Mockito.when(fileObjectRepository.findById(fileObjectFile.getId()))
                .thenReturn(Optional.ofNullable(fileObjectFile));
        Assertions.assertEquals(fileObjectFile, fileObjectApiService.findFileObject(fileObjectFile.getId()));
        Mockito.verify(fileObjectRepository, Mockito.times(1)).findById(fileObjectFile.getId());
    }

    @Test
    @DisplayName("JUnit test for findFileObject by id when fileObject does exists")
    void checkFindFileObjectByIdWhenFileObjectDoesNotExists() {
        Mockito.when(fileObjectRepository.findById(fileObjectFile.getId())).thenThrow(new NoSuchElementException());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> fileObjectApiService.findFileObject(fileObjectFile.getId()));
        Mockito.verify(fileObjectRepository, Mockito.times(1)).findById(fileObjectFile.getId());
    }

    @Test
    @DisplayName("JUnit test for findFileObject by name and path when fileObject exists " +
            "for returning fileObject is not null")
    void checkFindFileObjectByNameAndPathWhenFileObjectExistsReturnsNotNull() {
        Mockito.when(fileObjectRepository.findByNameAndPath(fileObjectFile.getName(), fileObjectFile.getPath()))
                .thenReturn(Optional.ofNullable(fileObjectFile));
        Assertions.assertNotNull(fileObjectApiService.findFileObject(fileObjectFile.getName(), fileObjectFile.getPath()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findByNameAndPath(fileObjectFile.getName(), fileObjectFile.getPath());
    }

    @Test
    @DisplayName("JUnit test for findFileObject by name and path when fileObject exists")
    void checkFindFileObjectByNameAndPathWhenFileObjectExists() {
        Mockito.when(fileObjectRepository.findByNameAndPath(fileObjectFile.getName(), fileObjectFile.getPath()))
                .thenReturn(Optional.ofNullable(fileObjectFile));
        Assertions.assertEquals(fileObjectFile, fileObjectApiService
                .findFileObject(fileObjectFile.getName(), fileObjectFile.getPath()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findByNameAndPath(fileObjectFile.getName(), fileObjectFile.getPath());
    }

    @Test
    @DisplayName("JUnit test for findFileObject by name and path when fileObject does exists")
    void checkFindFileObjectByNameAndPathWhenFileObjectDoesNotExists() {
        Mockito.when(fileObjectRepository.findByNameAndPath(fileObjectFile.getName(), fileObjectFile.getPath()))
                .thenThrow(new NoSuchElementException());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> fileObjectApiService.findFileObject(fileObjectFile.getName(), fileObjectFile.getPath()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findByNameAndPath(fileObjectFile.getName(), fileObjectFile.getPath());
    }

    @Test
    @DisplayName("JUnit test for findFileObject by name and creationTime when fileObject exists " +
            "for returning fileObject is not null")
    void checkFindFileObjectByNameAndCreationTimeWhenFileObjectExistsReturnsNotNull() {
        Mockito.when(fileObjectRepository
                        .findByNameAndCreationTime(fileObjectFile.getName(), fileObjectFile.getCreationTime()))
                .thenReturn(Optional.ofNullable(fileObjectFile));
        Assertions.assertNotNull(fileObjectApiService
                .findFileObjectByNameAndCreationTime(fileObjectFile.getName(), fileObjectFile.getCreationTime()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findByNameAndCreationTime(fileObjectFile.getName(), fileObjectFile.getCreationTime());
    }

    @Test
    @DisplayName("JUnit test for findFileObject by name and creationTime when fileObject exists")
    void checkFindFileObjectByNameAndCreationTimeWhenFileObjectExists() {
        Mockito.when(fileObjectRepository
                        .findByNameAndCreationTime(fileObjectFile.getName(), fileObjectFile.getCreationTime()))
                .thenReturn(Optional.ofNullable(fileObjectFile));
        Assertions.assertEquals(fileObjectFile, fileObjectApiService
                .findFileObjectByNameAndCreationTime(fileObjectFile.getName(), fileObjectFile.getCreationTime()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findByNameAndCreationTime(fileObjectFile.getName(), fileObjectFile.getCreationTime());
    }

    @Test
    @DisplayName("JUnit test for findFileObject by name and creationTime when fileObject does exists")
    void checkFindFileObjectByNameAndCreationTimeWhenFileObjectDoesNotExists() {
        Mockito.when(fileObjectRepository
                        .findByNameAndCreationTime(fileObjectFile.getName(), fileObjectFile.getCreationTime()))
                .thenThrow(new NoSuchElementException());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> fileObjectApiService.findFileObjectByNameAndCreationTime(fileObjectFile.getName(),
                        fileObjectFile.getCreationTime()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findByNameAndCreationTime(fileObjectFile.getName(), fileObjectFile.getCreationTime());
    }

    @Test
    @DisplayName("JUnit test for findFileObject by creationTime when fileObject exists " +
            "for returning fileObject is not null")
    void checkFindFileObjectByCreationTimeWhenFileObjectExistsReturnsNotNull() {
        Mockito.when(fileObjectRepository.findByCreationTime(fileObjectFile.getCreationTime()))
                .thenReturn(Optional.ofNullable(fileObjectFile));
        Assertions.assertNotNull(fileObjectApiService.findFileObjectByCreationTime(fileObjectFile.getCreationTime()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findByCreationTime(fileObjectFile.getCreationTime());
    }

    @Test
    @DisplayName("JUnit test for findFileObject by creationTime when fileObject exists")
    void checkFindFileObjectByCreationTimeWhenFileObjectExists() {
        Mockito.when(fileObjectRepository.findByCreationTime(fileObjectFile.getCreationTime()))
                .thenReturn(Optional.ofNullable(fileObjectFile));
        Assertions.assertEquals(fileObjectFile,
                fileObjectApiService.findFileObjectByCreationTime(fileObjectFile.getCreationTime()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findByCreationTime(fileObjectFile.getCreationTime());
    }

    @Test
    @DisplayName("JUnit test for findFileObject by creationTime when fileObject does exists")
    void checkFindFileObjectByCreationTimeWhenFileObjectDoesNotExists() {
        Mockito.when(fileObjectRepository.findByCreationTime(fileObjectFile.getCreationTime()))
                .thenThrow(new NoSuchElementException());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> fileObjectApiService.findFileObjectByCreationTime(fileObjectFile.getCreationTime()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findByCreationTime(fileObjectFile.getCreationTime());
    }

    @Test
    @DisplayName("JUnit test for findFileObject by pathRequest when fileObject exists " +
            "for returning fileObject is not null")
    void checkFindFileObjectByPathRequestTimeWhenFileObjectExistsReturnsNotNull() {
        PathRequest request = getPathRequestFile();
        File file = getFileFile();
        Mockito.when(fileMapper.pathToFile(request.getPath()))
                .thenReturn(file);
        Mockito.when(fileObjectRepository.findByNameAndPath(fileObjectFile.getName(), fileObjectFile.getPath()))
                .thenReturn(Optional.ofNullable(fileObjectFile));
        Assertions.assertNotNull(fileObjectApiService.findFileObjectByPathRequest(request));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findByNameAndPath(fileObjectFile.getName(), fileObjectFile.getPath());
    }

    @Test
    @DisplayName("JUnit test for findFileObject by pathRequest when fileObject exists")
    void checkFindFileObjectByPathRequestWhenFileObjectExists() {
        PathRequest request = getPathRequestFile();
        File file = getFileFile();
        Mockito.when(fileMapper.pathToFile(request.getPath()))
                .thenReturn(file);
        Mockito.when(fileObjectRepository.findByNameAndPath(fileObjectFile.getName(), fileObjectFile.getPath()))
                .thenReturn(Optional.ofNullable(fileObjectFile));
        Assertions.assertEquals(fileObjectFile, fileObjectApiService.findFileObjectByPathRequest(request));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findByNameAndPath(fileObjectFile.getName(), fileObjectFile.getPath());
    }

    @Test
    @DisplayName("JUnit test for findFileObject by pathRequest when fileObject does exists")
    void checkFindFileObjectByPathRequestWhenFileObjectDoesNotExists() {
        PathRequest request = getPathRequestFile();
        File file = getFileFile();
        Mockito.when(fileMapper.pathToFile(request.getPath()))
                .thenReturn(file);
        Mockito.when(fileObjectRepository.findByNameAndPath(fileObjectFile.getName(), fileObjectFile.getPath()))
                .thenThrow(new NoSuchElementException());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> fileObjectApiService.findFileObjectByPathRequest(request));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findByNameAndPath(fileObjectFile.getName(), fileObjectFile.getPath());
    }

    @Test
    @DisplayName("JUnit test for saveFileObject by fileObject for returning fileObject is not null")
    void checkSaveFileObjectByFileObjectReturnsNotNUll() {
        FileObject fileObjectWithoutId = getFileObjectFileWithoutId();
        Mockito.when(fileObjectRepository.save(fileObjectWithoutId)).
                thenReturn(fileObjectFile);
        Assertions.assertNotNull(fileObjectApiService.saveFileObject(fileObjectWithoutId));
        Mockito.verify(fileObjectRepository, Mockito.times(1)).save(fileObjectWithoutId);
    }

    @Test
    @DisplayName("JUnit test for saveFileObject by fileObject without id")
    void checkSaveFileObjectByFileObjectWithoutId() {
        FileObject fileObjectWithoutId = getFileObjectFileWithoutId();
        Mockito.when(fileObjectRepository.save(fileObjectWithoutId)).
                thenReturn(fileObjectFile);
        Assertions.assertEquals(fileObjectFile, fileObjectApiService.saveFileObject(fileObjectWithoutId));
        Mockito.verify(fileObjectRepository, Mockito.times(1)).save(fileObjectWithoutId);
    }

    @Test
    @DisplayName("JUnit test for saveFileObject by fileObject")
    void checkSaveFileObjectByFileObject() {
        Mockito.when(fileObjectRepository.save(fileObjectFile)).
                thenReturn(fileObjectFile);
        Assertions.assertEquals(fileObjectFile, fileObjectApiService.saveFileObject(fileObjectFile));
        Mockito.verify(fileObjectRepository, Mockito.times(1)).save(fileObjectFile);
    }

    @Test
    @DisplayName("JUnit test for deleteFileObject by id")
    void checkDeleteFileObjectById() {
        Mockito.doNothing().when(fileObjectRepository).deleteById(fileObjectFile.getId());
        fileObjectApiService.deleteFileObject(fileObjectFile.getId());
        Mockito.verify(fileObjectRepository, Mockito.times(1)).deleteById(fileObjectFile.getId());
    }

    @Test
    @DisplayName("JUnit test for findAllFileObjects method by idParent for returning not null")
    void checkFindAllFileObjectsByIdParentReturnsNotNull() {
        List<FileObject> fileObjects = getFileObjectsList();
        Mockito.when(fileObjectRepository.findAllByIdParent(fileObjectFile.getIdParent())).thenReturn(fileObjects);
        Assertions.assertNotNull(fileObjectApiService.findAllFileObjects(fileObjectFile.getIdParent()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findAllByIdParent(fileObjectFile.getIdParent());
    }

    @Test
    @DisplayName("JUnit test for findAllFileObjects method by idParent for returning empty")
    void checkFindAllFileObjectsByIdParentReturnsEmpty() {
        Mockito.when(fileObjectRepository.findAllByIdParent("62e7cd280c75de22fd3f1521")).thenReturn(List.of());
        Assertions.assertEquals(List.of(), fileObjectApiService.findAllFileObjects("62e7cd280c75de22fd3f1521"));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findAllByIdParent("62e7cd280c75de22fd3f1521");
    }

    @Test
    @DisplayName("JUnit test for findAllFileObjects method by idParent when fileObjects exist")
    void checkFindAllFileObjectsByIdParentWhenFileObjectsExist() {
        List<FileObject> fileObjects = getFileObjectsList();
        Mockito.when(fileObjectRepository.findAllByIdParent(fileObjectFile.getIdParent())).thenReturn(fileObjects);
        Assertions.assertEquals(fileObjects, fileObjectApiService.findAllFileObjects(fileObjectFile.getIdParent()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findAllByIdParent(fileObjectFile.getIdParent());
    }

    @Test
    @DisplayName("JUnit test for findAllFileObjects method by idParent when fileObjects exist for size")
    void checkFindAllFileObjectsByIdParentWhenFileObjectsExistForSize() {
        List<FileObject> fileObjects = getFileObjectsList();
        Mockito.when(fileObjectRepository.findAllByIdParent(fileObjectFile.getIdParent())).thenReturn(fileObjects);
        Assertions.assertEquals(fileObjects.size(),
                fileObjectApiService.findAllFileObjects(fileObjectFile.getIdParent()).size());
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findAllByIdParent(fileObjectFile.getIdParent());
    }

    @Test
    @DisplayName("JUnit test for findAllFileObjects method by idParent when fileObjects exist for first fileObject")
    void checkFindAllFileObjectsByIdParentWhenFileObjectsExistForFirstFileObject() {
        List<FileObject> fileObjects = getFileObjectsList();
        Mockito.when(fileObjectRepository.findAllByIdParent(fileObjectFile.getIdParent())).thenReturn(fileObjects);
        Assertions.assertEquals(fileObjects.get(0),
                fileObjectApiService.findAllFileObjects(fileObjectFile.getIdParent()).get(0));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .findAllByIdParent(fileObjectFile.getIdParent());
    }

    @Test
    @DisplayName("JUnit test for checkExistingFileObject by name and path when fileObject exists")
    void checkCheckExistingFileObjectByNameAndPathWhenFileObjectExists() {
        Mockito.when(fileObjectRepository.existsByNameAndPath(fileObjectFile.getName(), fileObjectFile.getPath()))
                .thenReturn(true);
        Assertions.assertTrue(fileObjectApiService
                .checkExistingFileObject(fileObjectFile.getName(), fileObjectFile.getPath()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .existsByNameAndPath(fileObjectFile.getName(), fileObjectFile.getPath());
    }

    @Test
    @DisplayName("JUnit test for checkExistingFileObject by name and path when fileObject does not exists")
    void checkCheckExistingFileObjectByNameAndPathWhenFileObjectDoesNotExists() {
        Mockito.when(fileObjectRepository.existsByNameAndPath(fileObjectFile.getName(), fileObjectFile.getPath()))
                .thenReturn(false);
        Assertions.assertFalse(fileObjectApiService
                .checkExistingFileObject(fileObjectFile.getName(), fileObjectFile.getPath()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .existsByNameAndPath(fileObjectFile.getName(), fileObjectFile.getPath());
    }

    @Test
    @DisplayName("JUnit test for checkExistingFileObjectByNameIdParentAndCreationTime when fileObject exists")
    void checkCheckExistingFileObjectByNameIdParentAndCreationTimeWhenFileObjectExists() {
        Mockito.when(fileObjectRepository.existsByNameAndIdParentAndCreationTime(fileObjectFile.getName(),
                        fileObjectFile.getIdParent(), fileObjectFile.getCreationTime()))
                .thenReturn(true);
        Assertions.assertTrue(fileObjectApiService
                .checkExistingFileObjectByNameIdParentAndCreationTime(fileObjectFile.getName(),
                        fileObjectFile.getIdParent(), fileObjectFile.getCreationTime()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .existsByNameAndIdParentAndCreationTime(fileObjectFile.getName(),
                        fileObjectFile.getIdParent(), fileObjectFile.getCreationTime());
    }

    @Test
    @DisplayName("JUnit test for checkExistingFileObjectByNameIdParentAndCreationTime when fileObject does not exists")
    void checkCheckExistingFileObjectByNameIdParentAndCreationTimeWhenFileObjectDoesNotExists() {
        Mockito.when(fileObjectRepository.existsByNameAndIdParentAndCreationTime(fileObjectFile.getName(),
                        fileObjectFile.getIdParent(), fileObjectFile.getCreationTime()))
                .thenReturn(false);
        Assertions.assertFalse(fileObjectApiService
                .checkExistingFileObjectByNameIdParentAndCreationTime(fileObjectFile.getName(),
                        fileObjectFile.getIdParent(), fileObjectFile.getCreationTime()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .existsByNameAndIdParentAndCreationTime(fileObjectFile.getName(),
                        fileObjectFile.getIdParent(), fileObjectFile.getCreationTime());
    }

    @Test
    @DisplayName("JUnit test for checkExistingFileObjectByIdParentAndCreationTime when fileObject exists")
    void checkCheckExistingFileObjectByIdParentAndCreationTimeWhenFileObjectExists() {
        Mockito.when(fileObjectRepository.existsByIdParentAndCreationTime(fileObjectFile.getIdParent(),
                        fileObjectFile.getCreationTime()))
                .thenReturn(true);
        Assertions.assertTrue(fileObjectApiService
                .checkExistingFileObjectByIdParentAndCreationTime(fileObjectFile.getIdParent(),
                        fileObjectFile.getCreationTime()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .existsByIdParentAndCreationTime(fileObjectFile.getIdParent(), fileObjectFile.getCreationTime());
    }

    @Test
    @DisplayName("JUnit test for checkExistingFileObjectByIdParentAndCreationTime when fileObject does not exists")
    void checkCheckExistingFileObjectByIdParentAndCreationTimeWhenFileObjectDoesNotExists() {
        Mockito.when(fileObjectRepository.existsByIdParentAndCreationTime(fileObjectFile.getIdParent(),
                        fileObjectFile.getCreationTime()))
                .thenReturn(false);
        Assertions.assertFalse(fileObjectApiService
                .checkExistingFileObjectByIdParentAndCreationTime(fileObjectFile.getIdParent(),
                        fileObjectFile.getCreationTime()));
        Mockito.verify(fileObjectRepository, Mockito.times(1))
                .existsByIdParentAndCreationTime(fileObjectFile.getIdParent(), fileObjectFile.getCreationTime());
    }

    @Test
    @DisplayName("JUnit test for buildFileObject by File for returning not null")
    void checkBuildFileObjectByFileReturnsNotNull() throws IOException {
        File file = getFileFile();
        CreationAndUpdatedTimeResponse response = getCreationAndUpdatedTimeResponseFile();
        Mockito.when(basicFileAttributesService.getCreationAndUpdatedTime(file)).thenReturn(response);
        Assertions.assertNotNull(fileObjectApiService.buildFileObject(file));
        Mockito.verify(basicFileAttributesService, Mockito.times(1))
                .getCreationAndUpdatedTime(file);
    }

    @Test
    @DisplayName("JUnit test for buildFileObject by File with id parent null")
    void checkBuildFileObjectByFileWithIdParentNull() throws IOException {
        File file = getFileFile();
        fileObjectFile.setId(null);
        fileObjectFile.setIdParent(null);
        fileObjectFile.setExtension(null);
        fileObjectFile.setType(Type.FOLDER);
        CreationAndUpdatedTimeResponse response = getCreationAndUpdatedTimeResponseFile();
        Mockito.when(basicFileAttributesService.getCreationAndUpdatedTime(file)).thenReturn(response);
        Assertions.assertEquals(fileObjectFile, fileObjectApiService.buildFileObject(file));
        Mockito.verify(basicFileAttributesService, Mockito.times(1))
                .getCreationAndUpdatedTime(file);
    }

    @Test
    @DisplayName("JUnit test for buildFileObject by File throws IOException")
    void checkBuildFileObjectByFileThrowsIOException() throws IOException {
        File file = getFileFile();
        Mockito.when(basicFileAttributesService.getCreationAndUpdatedTime(file)).thenThrow(new IOException());
        Assertions.assertThrows(IOException.class,
                () -> fileObjectApiService.buildFileObject(file));
        Mockito.verify(basicFileAttributesService, Mockito.times(1))
                .getCreationAndUpdatedTime(file);
    }


}
