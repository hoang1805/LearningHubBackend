package com.example.learninghubbackend.services.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    OWNER, ADMIN, TEACHER, STUDENT;

    public static Role fromString(String role) {
        try {
            return valueOf(role.toUpperCase());
        } catch (Exception e) {
            return STUDENT;
        }
    }

    public GrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + name());
    }
}
