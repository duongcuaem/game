package com.game.lyn.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
     private final HttpStatus status;
    private final String detail;

    public CustomException(String message, String detail, HttpStatus status) {
        super(message);
        this.status = status;
        this.detail = detail;
    }
}
