package com.example.learninghubbackend.commons.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidLogin extends CustomException {
    private static final String DEFAULT_MESSAGE = "Username or password is incorrect. Please try again.";
    private static final int DEFAULT_CODE = HttpStatus.BAD_REQUEST.value();

    public InvalidLogin() {
        super(DEFAULT_MESSAGE, DEFAULT_CODE);
    }
}
