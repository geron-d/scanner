package by.geron.scanner.service.scanner.filesystem;

import by.geron.scanner.dto.request.ScannerRequest;
import by.geron.scanner.entity.Acting;
import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.entity.Type;
import by.geron.scanner.mapper.file.FileMapper;
import by.geron.scanner.service.businesslog.BusinessLogService;
import by.geron.scanner.service.fileobject.FileObjectService;
import by.geron.scanner.service.scanner.database.ScannerDatabaseService;
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
public class ScannerFileSystemApiServiceTest {

    @Mock
    private FileObjectService fileObjectService;

    @Mock
    private BusinessLogService businessLogService;

    @Mock
    private FileMapper fileMapper;

    @Mock
    private ScannerDatabaseService scannerDatabaseService;

    @InjectMocks
    private ScannerFileSystemApiService scannerFileSystemApiService;

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

    private BusinessLog getBusinessLogCreated() {
        return BusinessLog.builder()
                .id("62e7cd280c75de22fd3f15d6")
                .fileObjectType(Type.FOLDER)
                .fileObjectId("62e7cd280c75de22fd3f15d6")
                .acting(Acting.CREATED)
                .oldName("1")
                .newName("1")
                .logDateTime(LocalDateTime.of(2022, 8, 1, 12, 55, 4, 0))
                .build();
    }

    private BusinessLog getBusinessLogRenamed() {
        return BusinessLog.builder()
                .id("62e7cd280c75de22fd3f15d6")
                .fileObjectType(Type.FOLDER)
                .fileObjectId("62e7cd280c75de22fd3f15d6")
                .acting(Acting.DELETED)
                .oldName("asd")
                .newName("2")
                .logDateTime(LocalDateTime.of(2022, 8, 1, 12, 55, 25, 0))
                .build();
    }

    private BusinessLog getBusinessLogUpdated() {
        return BusinessLog.builder()
                .id("62e7cd280c75de22fd3f15d6")
                .fileObjectType(Type.FOLDER)
                .fileObjectId("62e7cd280c75de22fd3f15d6")
                .acting(Acting.UPDATED)
                .oldName("1")
                .newName("1")
                .logDateTime(LocalDateTime.of(2022, 8, 1, 12, 55, 30, 0))
                .build();
    }

    @Test
    @DisplayName("JUnit test for scan by ScanRequest when scan folder and created for returning is not null")
    void checkScanDbByScanRequestWhenScanFolderCreatedReturnsNotNull() throws IOException {
        ScannerRequest scannerRequestParent = getScanRequestParent();
        File fileParent = getFileParent();
        FileObject fileObjectParent = getFileObjectParent();
        BusinessLog businessLog = getBusinessLogCreated();
        Mockito.when(scannerDatabaseService.scanDatabase(scannerRequestParent)).thenReturn(List.of(fileObjectParent.getId()));
        Mockito.when(fileMapper.pathToFile(scannerRequestParent.getPath())).thenReturn(fileParent);
        Mockito.when(fileObjectService.buildFileObject(fileParent)).thenReturn(fileObjectParent);
        Mockito.when(fileObjectService
                        .checkExistingFileObjectByNameIdParentAndCreationTime(fileObjectParent.getName(),
                                fileObjectParent.getIdParent(), fileObjectParent.getCreationTime()))
                .thenReturn(false);
        Mockito.when(fileObjectService
                .checkExistingFileObjectByIdParentAndCreationTime(fileObjectParent.getIdParent(),
                        fileObjectParent.getCreationTime())).thenReturn(false);
//        Mockito.when(fileObjectService.addFileObject(List.of(fileObjectParent.getId()), fileParent, fileObjectParent, List.of()))
//                .thenReturn(fileObjectParent);
        Mockito.when(fileObjectService.saveFileObject(fileObjectParent)).thenReturn(fileObjectParent);
//        Mockito.when(businessLogService.saveCreatedBusinessLog(fileObjectParent)).thenReturn(businessLog);
        Assertions.assertNotNull(scannerFileSystemApiService.scan(scannerRequestParent));
//        Mockito.verify(fileMapper, Mockito.times(1)).pathToFile(scanRequestParent.getPath());
//        Mockito.verify(fileObjectService, Mockito.times(1)).buildFileObject(fileParent);
//        Mockito.verify(fileObjectService, Mockito.times(1))
//                .checkExistingFileObjectByNameIdParentAndCreationTime(fileObjectParentBuild.getName(),
//                        fileObjectParentBuild.getIdParent(), fileObjectParentBuild.getCreationTime());
//        Mockito.verify(fileObjectService, Mockito.times(1))
//                .checkExistingFileObjectByIdParentAndCreationTime(fileObjectParentBuild.getIdParent(),
//                        fileObjectParentBuild.getCreationTime());
//        Mockito.verify(fileObjectService, Mockito.times(1))
//                .addFileObject(List.of(fileObjectParent.getId()), fileParent, fileObjectParent, List.of());
//        Mockito.verify(businessLogService, Mockito.times(1))
//                .saveCreatedBusinessLog(fileObjectParent);
    }

    @Test
    @DisplayName("JUnit test for scan by ScanRequest when scan folder and renamed for returning is not null")
    void checkScanDbByScanRequestWhenScanFolderRenamedReturnsNotNull() throws IOException {
        ScannerRequest scannerRequestParent = getScanRequestParent();
        File fileParent = getFileParent();
        FileObject fileObjectParent = getFileObjectParent();
        FileObject fileObjectDb = getFileObjectParent();
        fileObjectDb.setName("asd");
        FileObject fileObjectParentBuild = getFileObjectParent();
        fileObjectParentBuild.setId(null);
        BusinessLog businessLog = getBusinessLogRenamed();
        Mockito.when(fileMapper.pathToFile(scannerRequestParent.getPath())).thenReturn(fileParent);
        Mockito.when(fileObjectService.buildFileObject(fileParent)).thenReturn(fileObjectParentBuild);
        Mockito.when(fileObjectService
                        .checkExistingFileObjectByNameIdParentAndCreationTime(fileObjectParentBuild.getName(),
                                fileObjectParentBuild.getIdParent(), fileObjectParentBuild.getCreationTime()))
                .thenReturn(false);
        Mockito.when(fileObjectService
                .checkExistingFileObjectByIdParentAndCreationTime(fileObjectParentBuild.getIdParent(),
                        fileObjectParentBuild.getCreationTime())).thenReturn(true);
        Mockito.when(fileObjectService.findFileObjectByCreationTime(fileObjectParentBuild.getCreationTime()))
                .thenReturn(fileObjectDb);
        Mockito.when(businessLogService.saveRenamedBusinessLog(fileObjectParentBuild, fileObjectDb))
                .thenReturn(businessLog);
        Mockito.when(fileObjectService.addFileObject(List.of(), fileParent, fileObjectParentBuild, List.of()))
                .thenReturn(fileObjectParent);
        Assertions.assertNotNull(scannerFileSystemApiService.scan(scannerRequestParent));
        Mockito.verify(fileMapper, Mockito.times(1)).pathToFile(scannerRequestParent.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1)).buildFileObject(fileParent);
        Mockito.verify(fileObjectService, Mockito.times(1))
                .checkExistingFileObjectByNameIdParentAndCreationTime(fileObjectParentBuild.getName(),
                        fileObjectParentBuild.getIdParent(), fileObjectParentBuild.getCreationTime());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .checkExistingFileObjectByIdParentAndCreationTime(fileObjectParentBuild.getIdParent(),
                        fileObjectParentBuild.getCreationTime());
        Mockito.verify(fileObjectService, Mockito.times(1))
                .findFileObjectByCreationTime(fileObjectParentBuild.getCreationTime());
        Mockito.verify(businessLogService, Mockito.times(1))
                .saveRenamedBusinessLog(fileObjectParentBuild, fileObjectDb);
        Mockito.verify(fileObjectService, Mockito.times(1))
                .addFileObject(List.of(), fileParent, fileObjectParentBuild, List.of());
    }

    @Test
    @DisplayName("JUnit test for scan by ScanRequest when scan folder and updated for returning is not null")
    void checkScanDbByScanRequestWhenScanFolderUpdatedReturnsNotNull() throws IOException {
        ScannerRequest scannerRequestParent = getScanRequestParent();
        File fileParent = getFileParent();
        FileObject fileObjectParent = getFileObjectParent();
        FileObject fileObjectDb = getFileObjectParent();
        fileObjectDb.setUpdatedTime(LocalDateTime.of(2022, 8, 1, 11, 46, 0, 0));
        FileObject fileObjectParentBuild = getFileObjectParent();
        fileObjectParentBuild.setId(null);
        BusinessLog businessLog = getBusinessLogUpdated();
        Mockito.when(fileMapper.pathToFile(scannerRequestParent.getPath())).thenReturn(fileParent);
        Mockito.when(fileObjectService.buildFileObject(fileParent)).thenReturn(fileObjectParentBuild);
        Mockito.when(fileObjectService
                        .checkExistingFileObjectByNameIdParentAndCreationTime(fileObjectParentBuild.getName(),
                                fileObjectParentBuild.getIdParent(), fileObjectParentBuild.getCreationTime()))
                .thenReturn(true);
        Mockito.when(fileObjectService.findFileObjectByNameAndCreationTime(fileObjectParentBuild.getName(),
                        fileObjectParentBuild.getCreationTime()))
                .thenReturn(fileObjectDb);
        Mockito.when(businessLogService.saveUpdatedBusinessLog(fileObjectParentBuild, fileObjectDb))
                .thenReturn(businessLog);
        fileObjectParentBuild.setId(fileObjectParent.getId());
        Mockito.when(fileObjectService.addFileObject(List.of(), fileParent, fileObjectParentBuild, List.of()))
                .thenReturn(fileObjectParent);
        Assertions.assertNotNull(scannerFileSystemApiService.scan(scannerRequestParent));
        Mockito.verify(fileMapper, Mockito.times(1)).pathToFile(scannerRequestParent.getPath());
        Mockito.verify(fileObjectService, Mockito.times(1)).buildFileObject(fileParent);
        Mockito.verify(fileObjectService, Mockito.times(1))
                .checkExistingFileObjectByNameIdParentAndCreationTime(fileObjectParentBuild.getName(),
                        fileObjectParentBuild.getIdParent(), fileObjectParentBuild.getCreationTime());
        Mockito.verify(businessLogService, Mockito.times(1))
                .saveUpdatedBusinessLog(fileObjectParentBuild, fileObjectDb);
        Mockito.verify(fileObjectService, Mockito.times(1))
                .addFileObject(List.of(), fileParent, fileObjectParentBuild, List.of());
    }

}
