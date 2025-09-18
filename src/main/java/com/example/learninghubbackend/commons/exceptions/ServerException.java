package com.example.learninghubbackend.commons.exceptions;

import org.springframework.http.HttpStatus;

public class ServerException extends CustomException {

    public ServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
