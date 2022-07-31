package by.geron.scanner.service.fileAttributes;

import by.geron.scanner.dto.response.CreationAndUpdatedTimeResponse;
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
import java.util.LinkedHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicFileAttributesService {

    public LinkedHashMap<String, String> getMapBasicFileAttributes(File file) throws IOException {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        BasicFileAttributes basicFileAttributes = getBasicFileAttributes(file);
        map.put("creationTime", String.valueOf(basicFileAttributes.creationTime()));
        map.put("lastAccessTime", String.valueOf(basicFileAttributes.lastAccessTime()));
        map.put("lastModifiedTime", String.valueOf(basicFileAttributes.lastModifiedTime()));
        map.put("isRegularFile", String.valueOf(basicFileAttributes.isRegularFile()));
        map.put("isSymbolicLink", String.valueOf(basicFileAttributes.isSymbolicLink()));
        map.put("size", String.valueOf(basicFileAttributes.size()));
        return map;
    }

    public CreationAndUpdatedTimeResponse getCreationAndUpdatedTime(File file) throws IOException {
        BasicFileAttributes attributes = getBasicFileAttributes(file);
        return CreationAndUpdatedTimeResponse.builder()
                .creationTime(getCreationTime(attributes))
                .updatedTime(getUpdatedTime(attributes))
                .build();
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
}
