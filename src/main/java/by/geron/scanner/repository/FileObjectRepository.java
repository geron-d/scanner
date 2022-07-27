package by.geron.scanner.repository;

import by.geron.scanner.entity.FileObject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FileObjectRepository extends MongoRepository<FileObject, Long> {

    boolean existsByNameAndPath(String name, String path);

    Optional<FileObject> findByNameAndPath(String name, String path);

}
