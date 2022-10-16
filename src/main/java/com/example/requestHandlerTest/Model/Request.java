package com.example.requestHandlerTest.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "requests")
public class Request {

    @Id
    private long id;
    private String text;
    private long modifiedDate;
    private long length;

    public Request(String text) {
        this.id = this.hashCode();
        this.text = text;
    }

    public Request(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public Request() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
