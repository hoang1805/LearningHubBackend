package com.example.learninghubbackend.commons.exceptions;

import org.springframework.http.HttpStatus;

public class Unauthorized extends CustomException {
    public Unauthorized() {
        super("Unauthorized", HttpStatus.UNAUTHORIZED.value());
    }
}
