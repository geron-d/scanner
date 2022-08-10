package by.geron.scanner.service.scanner.database;

import by.geron.scanner.dto.request.ScannerRequest;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.entity.Type;
import by.geron.scanner.mapper.file.FileMapper;
import by.geron.scanner.mapper.scannerrequest.ScannerRequestMapper;
import by.geron.scanner.service.fileobject.FileObjectService;
import org.junit.jupiter.api.Assertions;
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
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing ScanDbApiService")
public class ScannerDatabaseApiServiceTest {

    @Mock
    private FileObjectService fileObjectService;

    @Mock
    private FileMapper fileMapper;

    @Mock
    ScannerRequestMapper scannerRequestMapper;

    @InjectMocks
    private ScannerDatabaseApiService scannerDatabaseApiService;

    private ScannerRequest getScanRequestParent() {
        return ScannerRequest.builder()
                .path("resources/1")
                .extensions(List.of())
                .build();
    }

    private ScannerRequest getScanRequestFile() {
        return ScannerRequest.builder()
                .path("resources/1/1.txt")
                .extensions(List.of())
                .build();
    }

    private File getFileParent() {
        return new File("resources/1");
    }

    private File getFileFile() {
        return new File("resources/1/1.txt");
    }

    private FileObject getFileObjectParent() {
        return FileObject.builder()
                .id("62e7cd280c75de22fd3f15d6")
                .idParent(null)
                .path("resources\\1")
                .name("1")
                .extension(null)
                .type(Type.FOLDER)
                .creationTime(LocalDateTime.of(2022, 7, 27, 8, 12, 55, 0))
                .updatedTime(LocalDateTime.of(2022, 8, 1, 11, 46, 3, 0))
                .build();
    }

    private FileObject getFileObjectFile() {
        return FileObject.builder()
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

    @Test
    @DisplayName("JUnit test for scanDb by ScanRequest when scan folder for returning is not null")
    void checkScanDbByScanRequestWhenScanFolderReturnsNotNull() throws IOException {
        ScannerRequest scannerRequestParent = getScanRequestParent();
        File fileParent = getFileParent();
        FileObject fileObjectParent = getFileObjectParent();
        List<FileObject> childFileObject = List.of();
        Mockito.when(fileMapper.pathToFile(scannerRequestParent.getPath())).thenReturn(fileParent);
        Mockito.when(fileObjectService.checkExistingFileObject(fileParent.getName(), fileParent.getPath()))
                .thenReturn(true);
        Mockito.when(fileObjectService.findFileObject(fileParent.getName(), fileParent.getPath())).thenReturn(fileObjectParent);
        Mockito.when(fileObjectService.findAllFileObjects(fileObjectParent.getId())).thenReturn(childFileObject);
        Assertions.assertNotNull(scannerDatabaseApiService.scanDatabase(scannerRequestParent));
        Mockito.verify(fileMapper, Mockito.times(1)).pathToFile(scannerRequestParent.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .checkExistingFileObject(fileParent.getName(), fileParent.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .findFileObject(fileParent.getName(), fileParent.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .findAllFileObjects(fileObjectParent.getId());
    }

    @Test
    @DisplayName("JUnit test for scanDb by ScanRequest when scan folder")
    void checkScanDbByScanRequestWhenScanFolder() throws IOException {
        ScannerRequest scannerRequestParent = getScanRequestParent();
        File fileParent = getFileParent();
        FileObject fileObjectParent = getFileObjectParent();
        List<FileObject> childFileObject = List.of();
        List<String> fileObjectsId = List.of(fileObjectParent.getId());
        Mockito.when(fileMapper.pathToFile(scannerRequestParent.getPath())).thenReturn(fileParent);
        Mockito.when(fileObjectService.checkExistingFileObject(fileParent.getName(), fileParent.getPath()))
                .thenReturn(true);
        Mockito.when(fileObjectService.findFileObject(fileParent.getName(), fileParent.getPath())).thenReturn(fileObjectParent);
        Mockito.when(fileObjectService.findAllFileObjects(fileObjectParent.getId())).thenReturn(childFileObject);
        Assertions.assertEquals(fileObjectsId, scannerDatabaseApiService.scanDatabase(scannerRequestParent));
        Mockito.verify(fileMapper, Mockito.times(1)).pathToFile(scannerRequestParent.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .checkExistingFileObject(fileParent.getName(), fileParent.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .findFileObject(fileParent.getName(), fileParent.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .findAllFileObjects(fileObjectParent.getId());
    }

    @Test
    @DisplayName("JUnit test for scanDb by ScanRequest when scan file for returning is not null")
    void checkScanDbByScanRequestWhenScanFileReturnsNotNull() throws IOException {
        ScannerRequest scannerRequestFile = getScanRequestFile();
        File fileFile = getFileFile();
        FileObject fileObjectFile = getFileObjectFile();
        Mockito.when(fileMapper.pathToFile(scannerRequestFile.getPath())).thenReturn(fileFile);
        Mockito.when(fileObjectService.checkExistingFileObject(fileFile.getName(), fileFile.getPath()))
                .thenReturn(true);
        Mockito.when(fileObjectService.findFileObject(fileFile.getName(), fileFile.getPath())).thenReturn(fileObjectFile);
        Assertions.assertNotNull(scannerDatabaseApiService.scanDatabase(scannerRequestFile));
        Mockito.verify(fileMapper, Mockito.times(1)).pathToFile(scannerRequestFile.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .checkExistingFileObject(fileFile.getName(), fileFile.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .findFileObject(fileFile.getName(), fileFile.getPath());
    }

    @Test
    @DisplayName("JUnit test for scanDb by ScanRequest when scan file")
    void checkScanDbByScanRequestWhenScanFile() throws IOException {
        ScannerRequest scannerRequestFile = getScanRequestFile();
        File fileFile = getFileFile();
        FileObject fileObjectFile = getFileObjectFile();
        List<String> fileObjectsId = List.of(fileObjectFile.getId());
        Mockito.when(fileMapper.pathToFile(scannerRequestFile.getPath())).thenReturn(fileFile);
        Mockito.when(fileObjectService.checkExistingFileObject(fileFile.getName(), fileFile.getPath()))
                .thenReturn(true);
        Mockito.when(fileObjectService.findFileObject(fileFile.getName(), fileFile.getPath())).thenReturn(fileObjectFile);
        Assertions.assertEquals(fileObjectsId, scannerDatabaseApiService.scanDatabase(scannerRequestFile));
        Mockito.verify(fileMapper, Mockito.times(1)).pathToFile(scannerRequestFile.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .checkExistingFileObject(fileFile.getName(), fileFile.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .findFileObject(fileFile.getName(), fileFile.getPath());
    }

    @Test
    @DisplayName("JUnit test for scanDb by path when scan folder for returning is not null")
    void checkScanDbByPathWhenScanFolderReturnsNotNull() throws IOException {
        String path = getScanRequestParent().getPath();
        ScannerRequest scannerRequestParent = getScanRequestParent();
        File fileParent = getFileParent();
        FileObject fileObjectParent = getFileObjectParent();
        List<FileObject> childFileObject = List.of();
        Mockito.when(scannerRequestMapper.pathToScanRequest(path)).thenReturn(scannerRequestParent);
        Mockito.when(fileMapper.pathToFile(scannerRequestParent.getPath())).thenReturn(fileParent);
        Mockito.when(fileObjectService.checkExistingFileObject(fileParent.getName(), fileParent.getPath()))
                .thenReturn(true);
        Mockito.when(fileObjectService.findFileObject(fileParent.getName(), fileParent.getPath())).thenReturn(fileObjectParent);
        Mockito.when(fileObjectService.findAllFileObjects(fileObjectParent.getId())).thenReturn(childFileObject);
        Assertions.assertNotNull(scannerDatabaseApiService.scanDatabase(path));
        Mockito.verify(scannerRequestMapper, Mockito.times(1)).pathToScanRequest(path);
        Mockito.verify(fileMapper, Mockito.times(1)).pathToFile(scannerRequestParent.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .checkExistingFileObject(fileParent.getName(), fileParent.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .findFileObject(fileParent.getName(), fileParent.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .findAllFileObjects(fileObjectParent.getId());
    }

    @Test
    @DisplayName("JUnit test for scanDb by path when scan folder")
    void checkScanDbByPathWhenScanFolder() throws IOException {
        String path = getScanRequestParent().getPath();
        ScannerRequest scannerRequestParent = getScanRequestParent();
        File fileParent = getFileParent();
        FileObject fileObjectParent = getFileObjectParent();
        List<FileObject> childFileObject = List.of();
        List<FileObject> fileObjects = List.of(fileObjectParent);
        Mockito.when(scannerRequestMapper.pathToScanRequest(path)).thenReturn(scannerRequestParent);
        Mockito.when(fileMapper.pathToFile(scannerRequestParent.getPath())).thenReturn(fileParent);
        Mockito.when(fileObjectService.checkExistingFileObject(fileParent.getName(), fileParent.getPath()))
                .thenReturn(true);
        Mockito.when(fileObjectService.findFileObject(fileParent.getName(), fileParent.getPath())).thenReturn(fileObjectParent);
        Mockito.when(fileObjectService.findAllFileObjects(fileObjectParent.getId())).thenReturn(childFileObject);
        Assertions.assertEquals(fileObjects, scannerDatabaseApiService.scanDatabase(path));
        Mockito.verify(scannerRequestMapper, Mockito.times(1)).pathToScanRequest(path);
        Mockito.verify(fileMapper, Mockito.times(1)).pathToFile(scannerRequestParent.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .checkExistingFileObject(fileParent.getName(), fileParent.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .findFileObject(fileParent.getName(), fileParent.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .findAllFileObjects(fileObjectParent.getId());
    }

    @Test
    @DisplayName("JUnit test for scanDb by path when scan file for returning is not null")
    void checkScanDbByPathWhenScanFileReturnsNotNull() throws IOException {
        String path = getScanRequestFile().getPath();
        ScannerRequest scannerRequestFile = getScanRequestFile();
        File fileFile = getFileFile();
        FileObject fileObjectFile = getFileObjectFile();
        Mockito.when(scannerRequestMapper.pathToScanRequest(path)).thenReturn(scannerRequestFile);
        Mockito.when(fileMapper.pathToFile(scannerRequestFile.getPath())).thenReturn(fileFile);
        Mockito.when(fileObjectService.checkExistingFileObject(fileFile.getName(), fileFile.getPath()))
                .thenReturn(true);
        Mockito.when(fileObjectService.findFileObject(fileFile.getName(), fileFile.getPath())).thenReturn(fileObjectFile);
        Assertions.assertNotNull(scannerDatabaseApiService.scanDatabase(path));
        Mockito.verify(scannerRequestMapper, Mockito.times(1)).pathToScanRequest(path);
        Mockito.verify(fileMapper, Mockito.times(1)).pathToFile(scannerRequestFile.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .checkExistingFileObject(fileFile.getName(), fileFile.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .findFileObject(fileFile.getName(), fileFile.getPath());
    }

    @Test
    @DisplayName("JUnit test for scanDb by path when scan file")
    void checkScanDbByPathWhenScanFile() throws IOException {
        String path = getScanRequestFile().getPath();
        ScannerRequest scannerRequestFile = getScanRequestFile();
        File fileFile = getFileFile();
        FileObject fileObjectFile = getFileObjectFile();
        List<FileObject> fileObjects = List.of(fileObjectFile);
        Mockito.when(scannerRequestMapper.pathToScanRequest(path)).thenReturn(scannerRequestFile);
        Mockito.when(fileMapper.pathToFile(scannerRequestFile.getPath())).thenReturn(fileFile);
        Mockito.when(fileObjectService.checkExistingFileObject(fileFile.getName(), fileFile.getPath()))
                .thenReturn(true);
        Mockito.when(fileObjectService.findFileObject(fileFile.getName(), fileFile.getPath())).thenReturn(fileObjectFile);
        Assertions.assertEquals(fileObjects, scannerDatabaseApiService.scanDatabase(path));
        Mockito.verify(scannerRequestMapper, Mockito.times(1)).pathToScanRequest(path);
        Mockito.verify(fileMapper, Mockito.times(1)).pathToFile(scannerRequestFile.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .checkExistingFileObject(fileFile.getName(), fileFile.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .findFileObject(fileFile.getName(), fileFile.getPath());
    }
}
