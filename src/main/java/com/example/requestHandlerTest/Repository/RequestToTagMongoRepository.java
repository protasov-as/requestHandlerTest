package com.example.requestHandlerTest.Repository;

import com.example.requestHandlerTest.Model.RequestToTag;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RequestToTagMongoRepository extends MongoRepository<RequestToTag, Long> {
    List<RequestToTag> findAllByRequestId(long requestId);
    List<RequestToTag> findAllByTagId(long tagId);
}
