package com.example.requestHandlerTest.Repository;

import com.example.requestHandlerTest.Model.RequestToFolder;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RequestToFolderMongoRepository extends MongoRepository<RequestToFolder, Long> {
    boolean existsByRequestId(long requestId);
    List<RequestToFolder> findAllByRequestId(long requestId);
    List<RequestToFolder> findAllByFolderId(long folderId);
}
