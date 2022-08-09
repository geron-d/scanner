package by.geron.scanner.repository.businesslog;

import by.geron.scanner.entity.BusinessLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BusinessLogRepository extends MongoRepository<BusinessLog, String> {

    List<BusinessLog> findAllByLogDateTimeBetween(LocalDateTime startLogDateTime, LocalDateTime finishLogDateTime);

    List<BusinessLog> findAllByLogDateTimeAfter(LocalDateTime startLogDateTime);

}
