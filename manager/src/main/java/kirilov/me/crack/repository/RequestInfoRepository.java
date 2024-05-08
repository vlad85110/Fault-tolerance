package kirilov.me.crack.repository;

import kirilov.me.crack.entity.RequestInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface RequestInfoRepository extends MongoRepository<RequestInfo, UUID> {}
