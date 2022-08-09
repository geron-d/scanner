package by.geron.scanner.repository.fileobject;

import by.geron.scanner.entity.FileObject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FileObjectRepository extends MongoRepository<FileObject, String> {

    Optional<FileObject> findByNameAndPath(String name, String path);

    Optional<FileObject> findByNameAndCreationTime(String name, LocalDateTime creationTime);

    Optional<FileObject> findByCreationTime(LocalDateTime creationTime);

    List<FileObject> findAllByIdParent(String idParent);

    boolean existsByNameAndPath(String name, String path);

    boolean existsByNameAndIdParentAndCreationTime(String name, String idParent, LocalDateTime creationTime);

    boolean existsByIdParentAndCreationTime(String idParent, LocalDateTime creationTime);

}
