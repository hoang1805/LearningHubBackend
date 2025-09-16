package com.example.learninghubbackend.services.user;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    OWNER, ADMIN, TEACHER, STUDENT;

    public static Role fromString(String role) {
        try {
            return valueOf(role.toUpperCase());
        } catch (Exception e) {
            return STUDENT;
        }
    }
}
