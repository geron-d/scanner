package by.geron.scanner.service.scanner.filesystem.savingutils;

import by.geron.scanner.entity.FileObject;
import by.geron.scanner.entity.Type;
import by.geron.scanner.service.fileobject.FileObjectApiService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing ScannerFileSystemSavingUtils")
public class ScannerFileSystemSavingUtilsTest {

    @Mock
    private FileObjectApiService fileObjectApiService;

    @InjectMocks
    private ScannerFileSystemSavingUtilsApi scannerFileSystemSavingUtilsApi;

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

    private File getFileFile() {
        return new File("resources\\1\\1.txt");
    }

    private File getFileFolder() {
        return new File("resources\\1\\2");
    }

    @Test
    @DisplayName("JUnit test for addFileObject when fileObject is FOLDER for returning not null")
    void checkAddFileObjectWhenFolderReturnNotNull() {
        List<String> fileObjects = new ArrayList<>();
        File file = getFileFolder();
        List<String> extensions = new ArrayList<>();
        Mockito.when(fileObjectApiService.saveFileObject(fileObjectFolder)).thenReturn(fileObjectFolder);
        Assertions.assertNotNull(scannerFileSystemSavingUtilsApi.addFileObject(fileObjects, file, fileObjectFolder, extensions));
        Mockito.verify(fileObjectApiService, Mockito.times(1)).saveFileObject(fileObjectFolder);
    }

    @Test
    @DisplayName("JUnit test for addFileObject when fileObject is FOLDER")
    void checkAddFileObjectWhenFolder() {
        List<String> fileObjects = new ArrayList<>();
        File file = getFileFolder();
        List<String> extensions = new ArrayList<>();
        Mockito.when(fileObjectApiService.saveFileObject(fileObjectFolder)).thenReturn(fileObjectFolder);
        Assertions.assertEquals(fileObjectFolder,
                scannerFileSystemSavingUtilsApi.addFileObject(fileObjects, file, fileObjectFolder, extensions));
        Mockito.verify(fileObjectApiService, Mockito.times(1)).saveFileObject(fileObjectFolder);
    }

    @Test
    @DisplayName("JUnit test for addFileObject when fileObject is FILE for returning not null")
    void checkAddFileObjectWhenFileReturnNotNull() {
        List<String> fileObjects = new ArrayList<>();
        File file = getFileFile();
        List<String> extensions = new ArrayList<>();
        Mockito.when(fileObjectApiService.saveFileObject(fileObjectFile)).thenReturn(fileObjectFile);
        Assertions.assertNotNull(scannerFileSystemSavingUtilsApi.addFileObject(fileObjects, file, fileObjectFile, extensions));
        Mockito.verify(fileObjectApiService, Mockito.times(1)).saveFileObject(fileObjectFile);
    }

    @Test
    @DisplayName("JUnit test for addFileObject when fileObject is FILE")
    void checkAddFileObjectWhenFile() {
        List<String> fileObjects = new ArrayList<>();
        File file = getFileFile();
        List<String> extensions = new ArrayList<>();
        Mockito.when(fileObjectApiService.saveFileObject(fileObjectFile)).thenReturn(fileObjectFile);
        Assertions.assertEquals(fileObjectFile,
                scannerFileSystemSavingUtilsApi.addFileObject(fileObjects, file, fileObjectFile, extensions));
        Mockito.verify(fileObjectApiService, Mockito.times(1)).saveFileObject(fileObjectFile);
    }

    @Test
    @DisplayName("JUnit test for addFileObject when fileObject is FILE with extensions")
    void checkAddFileObjectWhenFileWithExtensions() {
        List<String> fileObjects = new ArrayList<>();
        File file = getFileFile();
        List<String> extensions = new ArrayList<>();
        extensions.add(".txt");
        Assertions.assertEquals(fileObjectFile,
                scannerFileSystemSavingUtilsApi.addFileObject(fileObjects, file, fileObjectFile, extensions));
    }
}
