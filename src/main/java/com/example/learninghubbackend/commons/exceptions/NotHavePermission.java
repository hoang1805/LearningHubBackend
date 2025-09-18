package com.example.learninghubbackend.commons.exceptions;

import org.springframework.http.HttpStatus;

public class NotHavePermission extends CustomException {
    private static final String DEFAULT_MESSAGE = "You does not have permission%s. Please try again.";
    public NotHavePermission(String action) {
        super(String.format(DEFAULT_MESSAGE, " to" + action), HttpStatus.FORBIDDEN.value());
    }

    public NotHavePermission() {
        super(String.format(DEFAULT_MESSAGE, ""), HttpStatus.FORBIDDEN.value());
    }
}
