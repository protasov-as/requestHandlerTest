package com.example.requestHandlerTest.Repository;

import com.example.requestHandlerTest.Model.Folder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FolderMongoRepository extends MongoRepository<Folder, Long> {
}
