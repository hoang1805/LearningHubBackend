package com.example.learninghubbackend.commons.exceptions;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class InvalidField extends CustomException {
    public InvalidField(String field, String additional) {
        super(
                "Invalid " + field + (additional != null ? (". " + additional) : ""),
                HttpStatus.BAD_REQUEST.value()
        );
    }

    public InvalidField(String field) {
        super(
                "Invalid " + field + ". Please try again.",
                HttpStatus.BAD_REQUEST.value()
        );
    }
}
