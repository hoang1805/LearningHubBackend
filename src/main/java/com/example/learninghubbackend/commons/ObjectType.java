package com.example.learninghubbackend.commons;

import lombok.Getter;

@Getter
public enum ObjectType {
    USER("app.user"),
    SESSION("app.session"),
    ;

    private final String value;
    ObjectType(String s) {
        value = s;
    }
}
