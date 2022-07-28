package by.geron.scanner.service.fileObject;

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

@Slf4j
@Service
@RequiredArgsConstructor
public class FileObjectAttributesService {

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
