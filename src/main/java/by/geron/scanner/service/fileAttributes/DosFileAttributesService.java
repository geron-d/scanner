package by.geron.scanner.service.fileAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.DosFileAttributes;
import java.util.LinkedHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class DosFileAttributesService {

    public LinkedHashMap<String, String> getMapDosFileAttributes(File file) throws IOException {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        DosFileAttributes dosFileAttributes = getDosFileAttributes(file);
        map.put("isReadOnly", String.valueOf(dosFileAttributes.isReadOnly()));
        map.put("isHidden", String.valueOf(dosFileAttributes.isHidden()));
        map.put("isArchive", String.valueOf(dosFileAttributes.isArchive()));
        map.put("isSystem", String.valueOf(dosFileAttributes.isSystem()));
        return map;
    }

    private DosFileAttributes getDosFileAttributes(File file) throws IOException {
        return Files.readAttributes(file.toPath(), DosFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
    }
}
