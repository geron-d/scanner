package by.geron.scanner.repository.businessLog;

import by.geron.scanner.entity.BusinessLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BusinessLogRepository extends MongoRepository<BusinessLog, String> {

}
