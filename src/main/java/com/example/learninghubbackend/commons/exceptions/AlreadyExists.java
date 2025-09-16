package com.example.learninghubbackend.commons.exceptions;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class AlreadyExists extends CustomException {
    private static final String DEFAULT_MESSAGE = "%s ${value} already exists. Please try again.";
    public AlreadyExists(String field, Object value) {
        super(getField(field), HttpStatus.BAD_REQUEST.value(), Map.of("value", value));
    }

    private static String getField(String field) {
        return String.format(DEFAULT_MESSAGE, field.trim());
    }
}
