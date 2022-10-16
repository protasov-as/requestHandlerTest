package com.example.requestHandlerTest.Model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "requestsToTags")
public class RequestToTag {

    @Id
    private long id;
    private long requestId;
    private long tagId;

    public RequestToTag(long requestId, long tagId) {
        this.id = this.hashCode();
        this.requestId = requestId;
        this.tagId = tagId;
    }

    public RequestToTag() {
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

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }
}
