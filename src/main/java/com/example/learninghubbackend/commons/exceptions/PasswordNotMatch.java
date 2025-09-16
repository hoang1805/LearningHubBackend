package com.example.learninghubbackend.commons.exceptions;

import org.springframework.http.HttpStatus;

public class PasswordNotMatch extends CustomException {
    private static final String DEFAULT_MESSAGE = "Password does not match. Please try again.";
    private static final int DEFAULT_CODE = HttpStatus.BAD_REQUEST.value();

    public PasswordNotMatch() {
        super(DEFAULT_MESSAGE, DEFAULT_CODE);
    }
}
