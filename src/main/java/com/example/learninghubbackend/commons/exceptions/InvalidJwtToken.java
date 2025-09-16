package com.example.learninghubbackend.commons.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidJwtToken extends CustomException {

    public InvalidJwtToken() {
        super("Invalid token. Please refresh the page or login again.", HttpStatus.UNAUTHORIZED.value());
    }
}
