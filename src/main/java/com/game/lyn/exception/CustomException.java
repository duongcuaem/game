package com.game.lyn.exception;

import org.springframework.http.HttpStatus;


public class CustomException extends RuntimeException {
     private final HttpStatus status;
    private final String detail;

    public HttpStatus getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

    public CustomException(String message, String detail, HttpStatus status) {
        super(message);
        this.status = status;
        this.detail = detail;
    }
}
