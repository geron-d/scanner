package by.geron.scanner.repository.businesslog;

import by.geron.scanner.entity.BusinessLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

public interface BusinessLogRepository extends MongoRepository<BusinessLog, String> {

    Page<BusinessLog> findAllByLogDateTimeBetween(LocalDateTime startLogDateTime, LocalDateTime finishLogDateTime,
                                                  Pageable pageable);

    Page<BusinessLog> findAllByLogDateTimeAfter(LocalDateTime startLogDateTime, Pageable pageable);
}
