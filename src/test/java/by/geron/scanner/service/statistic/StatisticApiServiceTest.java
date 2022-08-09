package by.geron.scanner.service.statistic;

import by.geron.scanner.dto.request.ActingUserAfterRequest;
import by.geron.scanner.dto.request.ActingUserBetweenRequest;
import by.geron.scanner.dto.request.PathRequest;
import by.geron.scanner.dto.response.PathScannerStatisticResponse;
import by.geron.scanner.entity.Acting;
import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.entity.Type;
import by.geron.scanner.mapper.pathscannerstatisticresponse.PathScannerStatisticResponseMapper;
import by.geron.scanner.service.businesslog.BusinessLogService;
import by.geron.scanner.service.scanner.database.ScannerDatabaseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing ScanDbApiService")
public class StatisticApiServiceTest {

    @Mock
    private ScannerDatabaseService scannerDatabaseService;

    @Mock
    private BusinessLogService businessLogService;

    @Mock
    private PathScannerStatisticResponseMapper pathScannerStatisticResponseMapper;

    @InjectMocks
    private StatisticApiService statApiService;

    private PathRequest getPathRequest() {
        return PathRequest.builder()
                .path("resources/1")
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

    private FileObject getFileObjectFolder() {
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

    private List<FileObject> getFileObjects() {
        return List.of(getFileObjectParent(), getFileObjectFolder(), getFileObjectFile());
    }

    private Map<Type, Integer> getMapTypeStat() {
        Map<Type, Integer> typeStat = new HashMap<>();
        typeStat.put(Type.FILE, 1);
        typeStat.put(Type.FOLDER, 2);
        return typeStat;
    }

    private Map<String, Integer> getMapExtensionStat() {
        Map<String, Integer> extensionStat = new HashMap<>();
        extensionStat.put(".txt", 1);
        return extensionStat;
    }

    private PathScannerStatisticResponse getPathScanStatResponse() {
        return PathScannerStatisticResponse.builder()
                .typeStat(getMapTypeStat())
                .extensionStat(getMapExtensionStat())
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

    private BusinessLog getBusinessLogDeleted() {
        return BusinessLog.builder()
                .id("62e7cd280c75de22fd3f15d6")
                .fileObjectType(Type.FOLDER)
                .fileObjectId("62e7cd280c75de22fd3f15d6")
                .acting(Acting.DELETED)
                .oldName("1")
                .newName("1")
                .logDateTime(LocalDateTime.of(2022, 8, 1, 12, 55, 55, 0))
                .build();
    }

    private BusinessLog getBusinessLogRenamed() {
        return BusinessLog.builder()
                .id("62e7cd280c75de22fd3f15d6")
                .fileObjectType(Type.FOLDER)
                .fileObjectId("62e7cd280c75de22fd3f15d6")
                .acting(Acting.RENAMED)
                .oldName("1")
                .newName("2")
                .logDateTime(LocalDateTime.of(2022, 8, 1, 12, 55, 25, 0))
                .build();
    }

    private ActingUserBetweenRequest getActingUserBetweenRequest() {
        return ActingUserBetweenRequest.builder()
                .startLogDateTime(LocalDateTime.of(2022, 7, 27, 8, 12, 0, 0))
                .finishLogDateTime(LocalDateTime.of(2022, 7, 27, 8, 12, 59, 0))
                .build();
    }

    private ActingUserAfterRequest getActingUserAfterRequest() {
        return ActingUserAfterRequest.builder()
                .startLogDateTime(LocalDateTime.of(2022, 7, 27, 8, 12, 0, 0))
                .build();
    }

    private List<BusinessLog> getListBusinessLogs() {
        return List.of(getBusinessLogCreated(), getBusinessLogRenamed(), getBusinessLogUpdated(),
                getBusinessLogDeleted());
    }

    private Pageable getPageable() {
        return PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id"));
    }

    @Test
    @DisplayName("JUnit test for getPathScanStat by PathRequest for returning is not null")
    void checkGetPathScanStatByPathRequestReturnsNotNull() throws IOException {
        PathRequest pathRequest = getPathRequest();
        List<FileObject> fileObjects = getFileObjects();
        Map<Type, Integer> typeStat = getMapTypeStat();
        Map<String, Integer> extensionStat = getMapExtensionStat();
        PathScannerStatisticResponse pathScannerStatisticResponse = getPathScanStatResponse();
        Mockito.when(scannerDatabaseService.scanDatabase(pathRequest.getPath())).thenReturn(fileObjects);
        Mockito.when(pathScannerStatisticResponseMapper.mapsToPathScanStatResponseMapper(typeStat, extensionStat))
                .thenReturn(pathScannerStatisticResponse);
        Assertions.assertNotNull(statApiService.getPathScannerStatistic(pathRequest));
        Mockito.verify(scannerDatabaseService, Mockito.times(1)).scanDatabase(pathRequest.getPath());
        Mockito.verify(pathScannerStatisticResponseMapper, Mockito.times(1))
                .mapsToPathScanStatResponseMapper(typeStat, extensionStat);
    }

    @Test
    @DisplayName("JUnit test for getPathScanStat by PathRequest")
    void checkGetPathScanStatByPathRequest() throws IOException {
        PathRequest pathRequest = getPathRequest();
        List<FileObject> fileObjects = getFileObjects();
        Map<Type, Integer> typeStat = getMapTypeStat();
        Map<String, Integer> extensionStat = getMapExtensionStat();
        PathScannerStatisticResponse pathScannerStatisticResponse = getPathScanStatResponse();
        Mockito.when(scannerDatabaseService.scanDatabase(pathRequest.getPath())).thenReturn(fileObjects);
        Mockito.when(pathScannerStatisticResponseMapper.mapsToPathScanStatResponseMapper(typeStat, extensionStat))
                .thenReturn(pathScannerStatisticResponse);
        Assertions.assertEquals(pathScannerStatisticResponse, statApiService.getPathScannerStatistic(pathRequest));
        Mockito.verify(scannerDatabaseService, Mockito.times(1)).scanDatabase(pathRequest.getPath());
        Mockito.verify(pathScannerStatisticResponseMapper, Mockito.times(1))
                .mapsToPathScanStatResponseMapper(typeStat, extensionStat);
    }

    @Test
    @DisplayName("JUnit test for getActingUserStat by ActingUserBetweenRequest for returning is not null")
    void checkGetActingUserStatByActingUserBetweenRequestWhenScanFileReturnsNotNull() {
        ActingUserBetweenRequest actingUserBetweenRequest = getActingUserBetweenRequest();
        List<BusinessLog> businessLogs = getListBusinessLogs();
        Pageable pageable = getPageable();
        Mockito.when(businessLogService.findAllBusinessLog(actingUserBetweenRequest.getStartLogDateTime(),
                        actingUserBetweenRequest.getFinishLogDateTime(), pageable))
                .thenReturn(businessLogs);
        Assertions.assertNotNull(statApiService.getActingUserStatistic(actingUserBetweenRequest, pageable));
        Mockito.verify(businessLogService, Mockito.times(1))
                .findAllBusinessLog(actingUserBetweenRequest.getStartLogDateTime(),
                        actingUserBetweenRequest.getFinishLogDateTime(), pageable);
    }

    @Test
    @DisplayName("JUnit test for getActingUserStat by ActingUserBetweenRequest")
    void checkGetActingUserStatByActingUserBetweenRequestWhenScanFile() {
        ActingUserBetweenRequest actingUserBetweenRequest = getActingUserBetweenRequest();
        List<BusinessLog> businessLogs = getListBusinessLogs();
        Pageable pageable = getPageable();
        Mockito.when(businessLogService.findAllBusinessLog(actingUserBetweenRequest.getStartLogDateTime(),
                        actingUserBetweenRequest.getFinishLogDateTime(), pageable))
                .thenReturn(businessLogs);
        Assertions.assertEquals(businessLogs, statApiService.getActingUserStatistic(actingUserBetweenRequest, pageable));
        Mockito.verify(businessLogService, Mockito.times(1))
                .findAllBusinessLog(actingUserBetweenRequest.getStartLogDateTime(),
                        actingUserBetweenRequest.getFinishLogDateTime(), pageable);
    }

    @Test
    @DisplayName("JUnit test for getActingUserStat by ActingUserAfterRequest for returning is not null")
    void checkGetActingUserStatByActingUserAfterRequestWhenScanFileReturnsNotNull() {
        ActingUserAfterRequest actingUserAfterRequest = getActingUserAfterRequest();
        List<BusinessLog> businessLogs = getListBusinessLogs();
        Pageable pageable = getPageable();
        Mockito.when(businessLogService.findAllBusinessLog(actingUserAfterRequest.getStartLogDateTime(), pageable))
                .thenReturn(businessLogs);
        Assertions.assertNotNull(statApiService.getActingUserStatistic(actingUserAfterRequest, pageable));
        Mockito.verify(businessLogService, Mockito.times(1))
                .findAllBusinessLog(actingUserAfterRequest.getStartLogDateTime(), pageable);
    }

    @Test
    @DisplayName("JUnit test for getActingUserStat by ActingUserAfterRequest")
    void checkGetActingUserStatByActingUserAfterRequestWhenScanFile() {
        ActingUserAfterRequest actingUserAfterRequest = getActingUserAfterRequest();
        List<BusinessLog> businessLogs = getListBusinessLogs();
        Pageable pageable = getPageable();
        Mockito.when(businessLogService.findAllBusinessLog(actingUserAfterRequest.getStartLogDateTime(), pageable))
                .thenReturn(businessLogs);
        Assertions.assertEquals(businessLogs, statApiService.getActingUserStatistic(actingUserAfterRequest, pageable));
        Mockito.verify(businessLogService, Mockito.times(1))
                .findAllBusinessLog(actingUserAfterRequest.getStartLogDateTime(), pageable);
    }

}
