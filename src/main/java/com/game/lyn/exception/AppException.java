package com.game.lyn.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AppException extends RuntimeException {
    private ErrorDetails errorCode;

    public AppException(ErrorDetails errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}