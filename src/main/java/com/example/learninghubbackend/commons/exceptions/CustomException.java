package com.example.learninghubbackend.commons.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomException extends RuntimeException {
    private final int code;
    private final Map<String, Object> data;
    public CustomException(String message, int code, Map<String, Object> data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public CustomException(String message, int code) {
        super(message);
        this.code = code;
        this.data = null;
    }
}
