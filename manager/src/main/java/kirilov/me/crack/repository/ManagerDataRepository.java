package kirilov.me.crack.repository;

import kirilov.me.crack.entity.ManagerData;
import kirilov.me.crack.entity.RequestInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ManagerDataRepository extends MongoRepository<ManagerData, String> {}
