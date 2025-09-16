package com.example.learninghubbackend.commons.exceptions;

import org.springframework.http.HttpStatus;

public class TooManyRequests extends CustomException {
    public TooManyRequests() {
        super("Too many requests in this time. Please try again.", HttpStatus.TOO_MANY_REQUESTS.value());
    }
}
