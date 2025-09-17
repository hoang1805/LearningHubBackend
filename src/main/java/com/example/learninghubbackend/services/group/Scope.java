package com.example.learninghubbackend.services.group;

public enum Scope {
    PUBLIC, PRIVATE;

    public static Scope fromString(String scope) {
        try {
            return valueOf(scope.toUpperCase());
        } catch (Exception e) {
            return PRIVATE;
        }
    }
}
