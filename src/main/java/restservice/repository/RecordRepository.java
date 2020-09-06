package restservice.repository;

import org.springframework.data.repository.CrudRepository;
import restservice.model.Record;

public interface RecordRepository extends CrudRepository <Record, Integer>{
}
