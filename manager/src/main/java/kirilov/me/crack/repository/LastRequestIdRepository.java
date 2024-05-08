package kirilov.me.crack.repository;

import kirilov.me.crack.entity.LastRequestId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LastRequestIdRepository extends MongoRepository<LastRequestId, String> {

}
