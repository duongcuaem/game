package com.game.lyn.exception;

import java.util.Date;

public class ErrorDetails {

    private Date timestamp;
    private String message;
    private String detail;
    private String path;
    
    public ErrorDetails(Date timestamp, String message, String detail, String path) {
        this.timestamp = timestamp;
        this.message = message;
        this.detail = detail;
        this.path = path;
    }

    // Constructor mặc định
    public ErrorDetails() {
    }

    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
}