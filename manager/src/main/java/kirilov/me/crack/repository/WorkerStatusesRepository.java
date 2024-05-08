package kirilov.me.crack.repository;

import kirilov.me.crack.entity.WorkerStatuses;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkerStatusesRepository extends MongoRepository<WorkerStatuses, String> {}
