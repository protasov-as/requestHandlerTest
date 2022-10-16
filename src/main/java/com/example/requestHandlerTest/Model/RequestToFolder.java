package com.example.requestHandlerTest.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "requestsToFolders")
public class RequestToFolder {

    @Id
    private long id;
    private long requestId;
    private long folderId;

    public RequestToFolder() {
    }

    public RequestToFolder(long requestId, long folderId) {
        this.id = this.hashCode();
        this.requestId = requestId;
        this.folderId = folderId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public long getFolderId() {
        return folderId;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }
}
