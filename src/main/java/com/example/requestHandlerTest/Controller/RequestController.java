package com.example.requestHandlerTest.Controller;

import com.example.requestHandlerTest.Model.Request;
import com.example.requestHandlerTest.Repository.RequestMongoRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
public class RequestController {

    private RequestMongoRepository requestMongoRepository;

    @Autowired
    public RequestController(RequestMongoRepository requestMongoRepository) {
        this.requestMongoRepository = requestMongoRepository;
    }

    @ApiOperation(value = "Get all requests", notes = "Retrieves all requests from the collection")
    @GetMapping("/requests")
    public List<Request> getRequests(){
        return requestMongoRepository.findAll();
    }

    @ApiOperation(value = "Add a request", notes = "Saves a request with the specified values")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved"),
            @ApiResponse(code = 404, message = "Not found - The folder was not found")
    })
    @PostMapping("/requests")
    public ResponseEntity<Object> add(Request request) {
        if(request.getText().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(request.getId()==0) {
            request.setId(request.hashCode());
        }
        request.setModifiedDate(Instant.now().getEpochSecond());
        request.setLength(request.getText().length());
        requestMongoRepository.save(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
