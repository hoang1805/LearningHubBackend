package com.example.learninghubbackend.commons.exceptions;

import org.springframework.http.HttpStatus;

public class PasswordNotMatch extends CustomException {
    private static final String DEFAULT_MESSAGE = "%s does not match. Please try again.";
    private static final int DEFAULT_CODE = HttpStatus.BAD_REQUEST.value();

    public PasswordNotMatch() {
        super(String.format(DEFAULT_MESSAGE, "Password"), DEFAULT_CODE);
    }

    public PasswordNotMatch(String name) {
        super(String.format(DEFAULT_MESSAGE, name), DEFAULT_CODE);
    }
}
