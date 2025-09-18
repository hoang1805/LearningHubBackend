package com.example.learninghubbackend.commons.exceptions.group;

import com.example.learninghubbackend.commons.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class AlreadyIn extends CustomException {
    public AlreadyIn() {
        super("You are currently in this group.", HttpStatus.BAD_REQUEST.value());
    }
}
