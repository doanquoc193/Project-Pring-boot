package com.quoc.identity.service.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_KEY(
            9999,
            "Invalid message key",
            HttpStatus.BAD_REQUEST

    ),

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

    USER_NOT_EXISTED(
            1007,
            "User not existed",
            HttpStatus.BAD_REQUEST
    ),

    UNAUTHENTICATED(
            1008,
            "authenticated",
            HttpStatus.BAD_REQUEST
    ),


    INVALID_PASSWORD(
            1003,
            "Password must be at least 8 characters ",
            HttpStatus.BAD_REQUEST
    ),

    USERNAME_INVALID(
            1004,
            "Username must be at least 4 character",
            HttpStatus.BAD_REQUEST
    ),

    ROLE_NOT_FOUND(
            1005,
            "Role not found",
            HttpStatus.NOT_FOUND
    ),

    ROLE_EXISTED(
            1006,
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