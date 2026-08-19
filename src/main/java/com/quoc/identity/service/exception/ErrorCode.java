package com.quoc.identity.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter

public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(
            8888,
            "Uncategorized error",
            HttpStatus.INTERNAL_SERVER_ERROR

    ),

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
            "Authenticated",
            HttpStatus.UNAUTHORIZED
    ),

    UNAUTHORIZED(
            1009,
            "You do not have permission",
            HttpStatus.FORBIDDEN
    ),


    INVALID_PASSWORD(
            1003,
            "Password must be at least {min} characters ",
            HttpStatus.BAD_REQUEST
    ),

    INVALID_DOB(
            1010,
            "Your age must be at least {min}",
            HttpStatus.BAD_REQUEST
    ),

    USERNAME_INVALID(
            1004,
            "Username must be at least {min} character",
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
    private HttpStatusCode statusCode;

    ErrorCode(
            int code,
            String message,
            HttpStatusCode statusCode
    ) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }


}