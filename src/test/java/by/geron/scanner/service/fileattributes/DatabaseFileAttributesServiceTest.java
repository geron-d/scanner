package by.geron.scanner.service.fileattributes;

import by.geron.scanner.entity.FileObject;
import by.geron.scanner.entity.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing DatabaseFileAttributesServiceTest")
public class DatabaseFileAttributesServiceTest {

    @InjectMocks
    private DatabaseFileAttributesService databaseFileAttributesService;

    private FileObject fileObjectFile;

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

    private LinkedHashMap<String, String> getFileObjectsAttributes() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("file", fileObjectFile.getPath());
        map.put("id", fileObjectFile.getId());
        map.put("path", fileObjectFile.getPath());
        map.put("name", fileObjectFile.getName());
        map.put("idParent", fileObjectFile.getIdParent());
        map.put("extension", fileObjectFile.getExtension());
        map.put("creationTime", String.valueOf(fileObjectFile.getCreationTime()));
        map.put("updatedTime", String.valueOf(fileObjectFile.getUpdatedTime()));
        return map;
    }

    @Test
    @DisplayName("JUnit test for getDbFileAttributes for returning not null")
    void checkGetDbFileAttributesReturnNotNull() {
        Assertions.assertNotNull(databaseFileAttributesService.getDatabaseFileAttributes(fileObjectFile));
    }

    @Test
    @DisplayName("JUnit test for getDbFileAttributes")
    void checkGetDbFileAttributes() {
        LinkedHashMap<String, String> map = getFileObjectsAttributes();
        Assertions.assertEquals(map,
                databaseFileAttributesService.getDatabaseFileAttributes(fileObjectFile));
    }
}
