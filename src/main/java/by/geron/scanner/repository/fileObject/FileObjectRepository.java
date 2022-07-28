package by.geron.scanner.repository.fileObject;

import by.geron.scanner.entity.FileObject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FileObjectRepository extends MongoRepository<FileObject, String> {

    boolean existsByNameAndPath(String name, String path);

    Optional<FileObject> findByNameAndPath(String name, String path);

    List<FileObject> findAllByIdParent(String idParent);

}
