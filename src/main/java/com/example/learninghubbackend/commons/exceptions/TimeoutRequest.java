package com.example.learninghubbackend.commons.exceptions;

import org.springframework.http.HttpStatus;

public class TimeoutRequest extends CustomException{
    public TimeoutRequest() {
        super("Request timeout. Please try again.", HttpStatus.REQUEST_TIMEOUT.value());
    }
}
