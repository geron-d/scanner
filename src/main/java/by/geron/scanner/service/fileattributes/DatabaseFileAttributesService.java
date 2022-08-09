package by.geron.scanner.service.fileattributes;

import by.geron.scanner.entity.FileObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseFileAttributesService {

    public LinkedHashMap<String, String> getDatabaseFileAttributes(FileObject fileObject) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("file", fileObject.getPath());
        map.put("id", fileObject.getId());
        map.put("path", fileObject.getPath());
        map.put("name", fileObject.getName());
        map.put("idParent", fileObject.getIdParent());
        map.put("extension", fileObject.getExtension());
        map.put("creationTime", String.valueOf(fileObject.getCreationTime()));
        map.put("updatedTime", String.valueOf(fileObject.getUpdatedTime()));
        return map;
    }

}
