package com.example.requestHandlerTest.Repository;

import com.example.requestHandlerTest.Model.Request;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RequestMongoRepository extends MongoRepository<Request, Long> {
}
