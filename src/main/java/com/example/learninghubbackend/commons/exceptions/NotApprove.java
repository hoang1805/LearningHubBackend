package com.example.learninghubbackend.commons.exceptions;

import org.springframework.http.HttpStatus;

public class NotApprove extends CustomException {
    public NotApprove(String name) {
        super("You can not approve or reject this " + name, HttpStatus.BAD_REQUEST.value());
    }
}
