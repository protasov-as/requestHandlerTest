package com.example.requestHandlerTest.Controller;

import com.example.requestHandlerTest.Model.*;
import com.example.requestHandlerTest.Repository.RequestMongoRepository;
import com.example.requestHandlerTest.Repository.RequestToTagMongoRepository;
import com.example.requestHandlerTest.Repository.TagMongoRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TagController {

    @Value("${requestToTagLimit}")
    private int maxTagsPerRequest;
    private TagMongoRepository tagMongoRepository;
    private RequestToTagMongoRepository requestToTagMongoRepository;
    private RequestMongoRepository requestMongoRepository;

    @Autowired
    public TagController(TagMongoRepository tagMongoRepository, RequestToTagMongoRepository requestToTagMongoRepository, RequestMongoRepository requestMongoRepository) {
        this.tagMongoRepository = tagMongoRepository;
        this.requestToTagMongoRepository = requestToTagMongoRepository;
        this.requestMongoRepository = requestMongoRepository;
    }

    @ApiOperation(value = "Add a tag to a request", notes = "Adds a tag to a request with the specified id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved"),
            @ApiResponse(code = 404, message = "Not found - The folder was not found")
    })
    @PostMapping("/request2tag")
    public ResponseEntity<?> addTagToRequest(long requestId, long tagId) {
        if(tagMongoRepository.existsById(tagId)
                && requestMongoRepository.existsById(requestId)
                && requestToTagMongoRepository.findAllByRequestId(requestId).size() < maxTagsPerRequest) {
            return new ResponseEntity<>(requestToTagMongoRepository.save(new RequestToTag(requestId, tagId)), HttpStatus.CREATED);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }


    @ApiOperation(value = "Get all tags", notes = "Retrieves all tags from the collection")
    @GetMapping("/tags")
    public List<Tag> getTags(){
        return tagMongoRepository.findAll();
    }

    @ApiOperation(value = "Add a tag", notes = "Saves a tag with the specified values")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully saved"),
            @ApiResponse(code = 400, message = "Empty fields found!")
    })
    @PostMapping("/tags")
    public ResponseEntity<Object> add(Tag tag) {
        if(tag.getTagName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(tag.getId()==0) {
            tag.setId(tag.hashCode());
        }
        tagMongoRepository.save(tag);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get all requests by tag id", notes = "Returns all corresponding requests of a tag with the specified id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved"),
            @ApiResponse(code = 404, message = "Not found - The folder was not found")
    })
    @GetMapping("/tags/{id}")
    public ResponseEntity<?> getTag(@PathVariable("id") Long tagId){
        Optional<Tag> optionalList = tagMongoRepository.findById(tagId);
        if(!optionalList.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<RequestToTag> requestToTagArrayList = requestToTagMongoRepository.findAllByTagId(tagId);
        ArrayList<Optional<Request>> requestList = new ArrayList<>();
        requestToTagArrayList.forEach(item -> {
            requestList.add(requestMongoRepository.findById(item.getRequestId()));
        });
        return new ResponseEntity<>(requestList, HttpStatus.OK);
    }
}
