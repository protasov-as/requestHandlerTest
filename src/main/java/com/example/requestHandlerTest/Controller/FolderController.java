package com.example.requestHandlerTest.Controller;

import com.example.requestHandlerTest.Model.Folder;
import com.example.requestHandlerTest.Model.Request;
import com.example.requestHandlerTest.Model.RequestToFolder;
import com.example.requestHandlerTest.Repository.FolderMongoRepository;
import com.example.requestHandlerTest.Repository.RequestMongoRepository;
import com.example.requestHandlerTest.Repository.RequestToFolderMongoRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class FolderController {

    @Value("${requestToFolderLimit}")
    private int maxFoldersPerRequest;
    private FolderMongoRepository folderMongoRepository;
    private RequestToFolderMongoRepository requestToFolderMongoRepository;
    private RequestMongoRepository requestMongoRepository;

    @Autowired
    public FolderController(FolderMongoRepository folderMongoRepository, RequestToFolderMongoRepository requestToFolderMongoRepository, RequestMongoRepository requestMongoRepository) {
        this.folderMongoRepository = folderMongoRepository;
        this.requestToFolderMongoRepository = requestToFolderMongoRepository;
        this.requestMongoRepository = requestMongoRepository;
    }


    @ApiOperation(value = "Get all folders", notes = "Retrieves all folders from the collection")
    @GetMapping("/folders")
    public List<Folder> getFolders(){
        return folderMongoRepository.findAll();
    }

    @ApiOperation(value = "Add a folder", notes = "Saves a folder with the specified values")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully saved"),
            @ApiResponse(code = 400, message = "Empty fields found!")
    })
    @PostMapping("/folders")
    public ResponseEntity<Object> add(Folder folder) {
        if(folder.getFolderName().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(folder.getId()==0) {
            folder.setId(folder.hashCode());
        }
        folderMongoRepository.save(folder);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "Add a request to a folder", notes = "Saves a request to a folder with the specified id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved"),
            @ApiResponse(code = 404, message = "Not found - The folder was not found")
    })
    @PostMapping("/request2folder")
    public ResponseEntity<?> addRequestToFolder(long requestId, long folderId) {
        if(folderMongoRepository.existsById(folderId)
                && requestMongoRepository.existsById(requestId)
                && requestToFolderMongoRepository.findAllByRequestId(requestId).size() < maxFoldersPerRequest) {
            return new ResponseEntity<>(requestToFolderMongoRepository.save(new RequestToFolder(requestId, folderId)), HttpStatus.CREATED);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @ApiOperation(value = "Get all requests by folder id", notes = "Returns all corresponding requests of a folder with the specified id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved"),
            @ApiResponse(code = 404, message = "Not found - The folder was not found")
    })
    @GetMapping("/folders/{id}")
    public ResponseEntity<?> getFolder(@PathVariable("id") Long folderId){
        Optional<Folder> optionalList = folderMongoRepository.findById(folderId);
        if(!optionalList.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<RequestToFolder> requestToFolderArrayList = requestToFolderMongoRepository.findAllByFolderId(folderId);
        ArrayList<Optional<Request>> requestList = new ArrayList<>();
        requestToFolderArrayList.forEach(item -> {
            requestList.add(requestMongoRepository.findById(item.getRequestId()));
        });
        return new ResponseEntity<>(requestList, HttpStatus.OK);
    }
}
