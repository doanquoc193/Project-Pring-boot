package com.quoc.identity.service.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    USER_NOT_FOUND(
            1001,
            "User not found",
            HttpStatus.NOT_FOUND
    ),

    USER_EXISTED(
            1002,
            "User existed",
            HttpStatus.BAD_REQUEST
    ),

    ROLE_NOT_FOUND(
            1003,
            "Role not found",
            HttpStatus.NOT_FOUND
    ),

    ROLE_EXISTED(
            1004,
            "Role existed",
            HttpStatus.BAD_REQUEST
    );

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(
            int code,
            String message,
            HttpStatus httpStatus
    ) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}