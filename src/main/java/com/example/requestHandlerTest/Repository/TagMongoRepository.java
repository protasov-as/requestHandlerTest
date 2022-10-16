package com.example.requestHandlerTest.Repository;

import com.example.requestHandlerTest.Model.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TagMongoRepository extends MongoRepository<Tag, Long> {
}
