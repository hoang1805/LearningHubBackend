package com.example.learninghubbackend.services.user;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    MALE, FEMALE, OTHER;

    @JsonCreator
    public static Gender fromString(String gender) {
        try {
            return valueOf(gender.toUpperCase());
        } catch (Exception e) {
            return OTHER;
        }
    }
}
