package by.geron.scanner.service.businesslog;

import by.geron.scanner.dto.request.ActingUserBetweenRequest;
import by.geron.scanner.entity.Acting;
import by.geron.scanner.entity.BusinessLog;
import by.geron.scanner.entity.FileObject;
import by.geron.scanner.entity.Type;
import by.geron.scanner.mapper.businesslog.BusinessLogMapper;
import by.geron.scanner.repository.businesslog.BusinessLogRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing BusinessLog service")
public class BusinessLogApiTest {

    @Mock
    private BusinessLogRepository businessLogRepository;

    @Mock
    private BusinessLogMapper businessLogMapper;

    @InjectMocks
    private BusinessLogApiService businessLogApiService;

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

    private FileObject getFileObject() {
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

    private ActingUserBetweenRequest getActingUserBetweenRequest() {
        return ActingUserBetweenRequest.builder()
                .startLogDateTime(LocalDateTime.of(2022, 7, 27, 8, 12, 0, 0))
                .finishLogDateTime(LocalDateTime.of(2022, 7, 27, 8, 12, 59, 0))
                .build();
    }

    private List<BusinessLog> getListBusinessLogs() {
        return List.of(getBusinessLogCreated(), getBusinessLogRenamed(), getBusinessLogUpdated(),
                getBusinessLogDeleted());
    }

    @Test
    @DisplayName("JUnit test for saveCreatedBusinessLog returning BusinessLog is not null")
    void checkSaveCreatedBusinessLogReturnsNotNUll() {
        FileObject fileObject = getFileObject();
        BusinessLog businessLog = getBusinessLogCreated();
        BusinessLog businessLogWithoutId = getBusinessLogCreated();
        businessLogWithoutId.setId(null);
        Mockito.when(businessLogMapper.fileObjectToCreatedBusinessLog(fileObject)).thenReturn(businessLogWithoutId);
        Mockito.when(businessLogRepository.save(businessLogWithoutId)).thenReturn(businessLog);
        Assertions.assertNotNull(businessLogApiService.saveCreatedBusinessLog(fileObject));
        Mockito.verify(businessLogMapper, Mockito.times(1))
                .fileObjectToCreatedBusinessLog(fileObject);
        Mockito.verify(businessLogRepository, Mockito.times(1)).save(businessLogWithoutId);
    }

    @Test
    @DisplayName("JUnit test for saveCreatedBusinessLog")
    void checkSaveCreatedBusinessLog() {
        FileObject fileObject = getFileObject();
        BusinessLog businessLog = getBusinessLogCreated();
        BusinessLog businessLogWithoutId = getBusinessLogCreated();
        businessLogWithoutId.setId(null);
        Mockito.when(businessLogMapper.fileObjectToCreatedBusinessLog(fileObject)).thenReturn(businessLogWithoutId);
        Mockito.when(businessLogRepository.save(businessLogWithoutId)).thenReturn(businessLog);
        Assertions.assertEquals(businessLog, businessLogApiService.saveCreatedBusinessLog(fileObject));
        Mockito.verify(businessLogMapper, Mockito.times(1))
                .fileObjectToCreatedBusinessLog(fileObject);
        Mockito.verify(businessLogRepository, Mockito.times(1)).save(businessLogWithoutId);
    }

    @Test
    @DisplayName("JUnit test for saveUpdatedBusinessLog returning BusinessLog is not null")
    void checkSaveUpdatedBusinessLogReturnsNotNUll() {
        FileObject fileObject = getFileObject();
        BusinessLog businessLog = getBusinessLogUpdated();
        BusinessLog businessLogWithoutId = getBusinessLogUpdated();
        businessLogWithoutId.setId(null);
        Mockito.when(businessLogMapper.fileObjectsToUpdatedBusinessLog(fileObject, fileObject))
                .thenReturn(businessLogWithoutId);
        Mockito.when(businessLogRepository.save(businessLogWithoutId)).thenReturn(businessLog);
        Assertions.assertNotNull(businessLogApiService.saveUpdatedBusinessLog(fileObject, fileObject));
        Mockito.verify(businessLogMapper, Mockito.times(1))
                .fileObjectsToUpdatedBusinessLog(fileObject, fileObject);
        Mockito.verify(businessLogRepository, Mockito.times(1)).save(businessLogWithoutId);
    }

    @Test
    @DisplayName("JUnit test for saveUpdatedBusinessLog")
    void checkSaveUpdatedBusinessLog() {
        FileObject fileObject = getFileObject();
        BusinessLog businessLog = getBusinessLogUpdated();
        BusinessLog businessLogWithoutId = getBusinessLogUpdated();
        businessLogWithoutId.setId(null);
        Mockito.when(businessLogMapper.fileObjectsToUpdatedBusinessLog(fileObject, fileObject))
                .thenReturn(businessLogWithoutId);
        Mockito.when(businessLogRepository.save(businessLogWithoutId)).thenReturn(businessLog);
        Assertions.assertEquals(businessLog, businessLogApiService.saveUpdatedBusinessLog(fileObject, fileObject));
        Mockito.verify(businessLogMapper, Mockito.times(1))
                .fileObjectsToUpdatedBusinessLog(fileObject, fileObject);
        Mockito.verify(businessLogRepository, Mockito.times(1)).save(businessLogWithoutId);
    }

    @Test
    @DisplayName("JUnit test for saveDeletedBusinessLog returning BusinessLog is not null")
    void checkSaveDeletedBusinessLogReturnsNotNUll() {
        FileObject fileObject = getFileObject();
        BusinessLog businessLog = getBusinessLogDeleted();
        BusinessLog businessLogWithoutId = getBusinessLogDeleted();
        businessLogWithoutId.setId(null);
        Mockito.when(businessLogMapper.fileObjectToDeletedBusinessLog(fileObject)).thenReturn(businessLogWithoutId);
        Mockito.when(businessLogRepository.save(businessLogWithoutId)).thenReturn(businessLog);
        Assertions.assertNotNull(businessLogApiService.saveDeletedBusinessLog(fileObject));
        Mockito.verify(businessLogMapper, Mockito.times(1))
                .fileObjectToDeletedBusinessLog(fileObject);
        Mockito.verify(businessLogRepository, Mockito.times(1)).save(businessLogWithoutId);
    }

    @Test
    @DisplayName("JUnit test for saveDeletedBusinessLog")
    void checkSaveDeletedBusinessLog() {
        FileObject fileObject = getFileObject();
        BusinessLog businessLog = getBusinessLogUpdated();
        BusinessLog businessLogWithoutId = getBusinessLogUpdated();
        businessLogWithoutId.setId(null);
        Mockito.when(businessLogMapper.fileObjectToCreatedBusinessLog(fileObject)).thenReturn(businessLogWithoutId);
        Mockito.when(businessLogRepository.save(businessLogWithoutId)).thenReturn(businessLog);
        Assertions.assertEquals(businessLog, businessLogApiService.saveCreatedBusinessLog(fileObject));
        Mockito.verify(businessLogMapper, Mockito.times(1))
                .fileObjectToCreatedBusinessLog(fileObject);
        Mockito.verify(businessLogRepository, Mockito.times(1)).save(businessLogWithoutId);
    }

    @Test
    @DisplayName("JUnit test for saveRenamedBusinessLog returning BusinessLog is not null")
    void checkSaveRenamedBusinessLogReturnsNotNUll() {
        FileObject fileObject = getFileObject();
        BusinessLog businessLog = getBusinessLogRenamed();
        BusinessLog businessLogWithoutId = getBusinessLogRenamed();
        businessLogWithoutId.setId(null);
        Mockito.when(businessLogMapper.fileObjectsToRenamedBusinessLog(fileObject, fileObject))
                .thenReturn(businessLogWithoutId);
        Mockito.when(businessLogRepository.save(businessLogWithoutId)).thenReturn(businessLog);
        Assertions.assertNotNull(businessLogApiService.saveRenamedBusinessLog(fileObject,fileObject));
        Mockito.verify(businessLogMapper, Mockito.times(1))
                .fileObjectsToRenamedBusinessLog(fileObject, fileObject);
        Mockito.verify(businessLogRepository, Mockito.times(1)).save(businessLogWithoutId);
    }

    @Test
    @DisplayName("JUnit test for saveRenamedBusinessLog")
    void checkSaveRenamedBusinessLog() {
        FileObject fileObject = getFileObject();
        BusinessLog businessLog = getBusinessLogRenamed();
        BusinessLog businessLogWithoutId = getBusinessLogRenamed();
        businessLogWithoutId.setId(null);
        Mockito.when(businessLogMapper.fileObjectsToRenamedBusinessLog(fileObject, fileObject))
                .thenReturn(businessLogWithoutId);
        Mockito.when(businessLogRepository.save(businessLogWithoutId)).thenReturn(businessLog);
        Assertions.assertEquals(businessLog, businessLogApiService.saveRenamedBusinessLog(fileObject, fileObject));
        Mockito.verify(businessLogMapper, Mockito.times(1))
                .fileObjectsToRenamedBusinessLog(fileObject, fileObject);
        Mockito.verify(businessLogRepository, Mockito.times(1)).save(businessLogWithoutId);
    }

    @Test
    @DisplayName("JUnit test for findAllBusinessLog method by startLogDateTime and finishLogDateTime " +
            "for returning not null")
    void checkFindAllBusinessLogByStartLogDateTimeAndFinishLogDateTimeReturnsNotNull() {
        List<BusinessLog> businessLogs = getListBusinessLogs();
        ActingUserBetweenRequest request = getActingUserBetweenRequest();
        Mockito.when(businessLogRepository
                .findAllByLogDateTimeBetween(request.getStartLogDateTime(), request.getFinishLogDateTime()))
                .thenReturn(businessLogs);
        Assertions.assertNotNull(businessLogApiService
                .findAllBusinessLog(request.getStartLogDateTime(), request.getFinishLogDateTime()));
        Mockito.verify(businessLogRepository, Mockito.times(1))
                .findAllByLogDateTimeBetween(request.getStartLogDateTime(), request.getFinishLogDateTime());
    }

    @Test
    @DisplayName("JUnit test for findAllBusinessLog method by startLogDateTime and finishLogDateTime " +
            "for returning empty")
    void checkFindAllBusinessLogByStartLogDateTimeAndFinishLogDateTimeReturnsEmpty() {
        ActingUserBetweenRequest request = getActingUserBetweenRequest();
        Mockito.when(businessLogRepository
                        .findAllByLogDateTimeBetween(request.getStartLogDateTime(), request.getFinishLogDateTime()))
                .thenReturn(List.of());
        Assertions.assertEquals(List.of(),
                businessLogApiService.findAllBusinessLog(request.getStartLogDateTime(),
                        request.getFinishLogDateTime()));
        Mockito.verify(businessLogRepository, Mockito.times(1))
                .findAllByLogDateTimeBetween(request.getStartLogDateTime(), request.getFinishLogDateTime());
    }

    @Test
    @DisplayName("JUnit test for findAllBusinessLog method by startLogDateTime and finishLogDateTime " +
            "when BusinessLogs exist")
    void checkFindAllBusinessLogByStartLogDateTimeAndFinishLogDateTimeWhenBusinessLogsExist() {
        List<BusinessLog> businessLogs = getListBusinessLogs();
        ActingUserBetweenRequest request = getActingUserBetweenRequest();
        Mockito.when(businessLogRepository
                        .findAllByLogDateTimeBetween(request.getStartLogDateTime(), request.getFinishLogDateTime()))
                .thenReturn(businessLogs);
        Assertions.assertEquals(businessLogs,
                businessLogApiService.findAllBusinessLog(request.getStartLogDateTime(),
                        request.getFinishLogDateTime()));
        Mockito.verify(businessLogRepository, Mockito.times(1))
                .findAllByLogDateTimeBetween(request.getStartLogDateTime(), request.getFinishLogDateTime());
    }

    @Test
    @DisplayName("JUnit test for findAllBusinessLog method by startLogDateTime and finishLogDateTime " +
            "when BusinessLogs exist for size")
    void checkFindAllBusinessLogByStartLogDateTimeAndFinishLogDateTimeWhenBusinessLogsExistForSize() {
        List<BusinessLog> businessLogs = getListBusinessLogs();
        ActingUserBetweenRequest request = getActingUserBetweenRequest();
        Mockito.when(businessLogRepository
                        .findAllByLogDateTimeBetween(request.getStartLogDateTime(), request.getFinishLogDateTime()))
                .thenReturn(businessLogs);
        Assertions.assertEquals(businessLogs.size(),
                businessLogApiService.findAllBusinessLog(request.getStartLogDateTime(),
                        request.getFinishLogDateTime()).size());
        Mockito.verify(businessLogRepository, Mockito.times(1))
                .findAllByLogDateTimeBetween(request.getStartLogDateTime(), request.getFinishLogDateTime());
    }

    @Test
    @DisplayName("JUnit test for findAllBusinessLog method by startLogDateTime and finishLogDateTime " +
            "when BusinessLogs exist for first BusinessLog")
    void checkFindAllBusinessLogByStartLogDateTimeAndFinishLogDateTimeWhenBusinessLogsExistForFirstBusinessLog() {
        List<BusinessLog> businessLogs = getListBusinessLogs();
        ActingUserBetweenRequest request = getActingUserBetweenRequest();
        Mockito.when(businessLogRepository
                        .findAllByLogDateTimeBetween(request.getStartLogDateTime(), request.getFinishLogDateTime()))
                .thenReturn(businessLogs);
        Assertions.assertEquals(businessLogs.get(0),
                businessLogApiService.findAllBusinessLog(request.getStartLogDateTime(),
                        request.getFinishLogDateTime()).get(0));
        Mockito.verify(businessLogRepository, Mockito.times(1))
                .findAllByLogDateTimeBetween(request.getStartLogDateTime(), request.getFinishLogDateTime());
    }

    @Test
    @DisplayName("JUnit test for findAllBusinessLog method by startLogDateTime for returning not null")
    void checkFindAllBusinessLogByStartLogDateTimeReturnsNotNull() {
        List<BusinessLog> businessLogs = getListBusinessLogs();
        ActingUserBetweenRequest request = getActingUserBetweenRequest();
        Mockito.when(businessLogRepository.findAllByLogDateTimeAfter(request.getStartLogDateTime()))
                .thenReturn(businessLogs);
        Assertions.assertNotNull(businessLogApiService.findAllBusinessLog(request.getStartLogDateTime()));
        Mockito.verify(businessLogRepository, Mockito.times(1))
                .findAllByLogDateTimeAfter(request.getStartLogDateTime());
    }

    @Test
    @DisplayName("JUnit test for findAllBusinessLog method by startLogDateTime for returning empty")
    void checkFindAllBusinessLogByStartLogDateTimeReturnsEmpty() {
        ActingUserBetweenRequest request = getActingUserBetweenRequest();
        Mockito.when(businessLogRepository.findAllByLogDateTimeAfter(request.getStartLogDateTime()))
                .thenReturn(List.of());
        Assertions.assertEquals(List.of(), businessLogApiService.findAllBusinessLog(request.getStartLogDateTime()));
        Mockito.verify(businessLogRepository, Mockito.times(1))
                .findAllByLogDateTimeAfter(request.getStartLogDateTime());
    }

    @Test
    @DisplayName("JUnit test for findAllBusinessLog method by startLogDateTime when BusinessLogs exist")
    void checkFindAllBusinessLogByStartLogDateTimeWhenBusinessLogsExist() {
        List<BusinessLog> businessLogs = getListBusinessLogs();
        ActingUserBetweenRequest request = getActingUserBetweenRequest();
        Mockito.when(businessLogRepository.findAllByLogDateTimeAfter(request.getStartLogDateTime()))
                .thenReturn(businessLogs);
        Assertions.assertEquals(businessLogs, businessLogApiService.findAllBusinessLog(request.getStartLogDateTime()));
        Mockito.verify(businessLogRepository, Mockito.times(1))
                .findAllByLogDateTimeAfter(request.getStartLogDateTime());
    }

    @Test
    @DisplayName("JUnit test for findAllBusinessLog method by startLogDateTime when BusinessLogs exist for size")
    void checkFindAllBusinessLogByStartLogDateTimeWhenBusinessLogsExistForSize() {
        List<BusinessLog> businessLogs = getListBusinessLogs();
        ActingUserBetweenRequest request = getActingUserBetweenRequest();
        Mockito.when(businessLogRepository.findAllByLogDateTimeAfter(request.getStartLogDateTime()))
                .thenReturn(businessLogs);
        Assertions.assertEquals(businessLogs.size(),
                businessLogApiService.findAllBusinessLog(request.getStartLogDateTime()).size());
        Mockito.verify(businessLogRepository, Mockito.times(1))
                .findAllByLogDateTimeAfter(request.getStartLogDateTime());
    }

    @Test
    @DisplayName("JUnit test for findAllBusinessLog method by startLogDateTime when BusinessLogs exist " +
            "for first BusinessLog")
    void checkFindAllBusinessLogByStartLogDateTimeWhenBusinessLogsExistForFirstBusinessLog() {
        List<BusinessLog> businessLogs = getListBusinessLogs();
        ActingUserBetweenRequest request = getActingUserBetweenRequest();
        Mockito.when(businessLogRepository.findAllByLogDateTimeAfter(request.getStartLogDateTime()))
                .thenReturn(businessLogs);
        Assertions.assertEquals(businessLogs.get(0),
                businessLogApiService.findAllBusinessLog(request.getStartLogDateTime()).get(0));
        Mockito.verify(businessLogRepository, Mockito.times(1))
                .findAllByLogDateTimeAfter(request.getStartLogDateTime());
    }
}
