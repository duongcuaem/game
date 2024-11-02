package com.game.lyn.exception;
public class AppException extends RuntimeException {
    private ErrorDetails errorCode;

    public ErrorDetails getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorDetails errorCode) {
        this.errorCode = errorCode;
    }

    public AppException(ErrorDetails errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}