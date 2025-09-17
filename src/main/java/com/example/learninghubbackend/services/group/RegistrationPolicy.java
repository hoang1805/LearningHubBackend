package com.example.learninghubbackend.services.group;

public enum RegistrationPolicy {
    // Public scope
    OPEN, REQUEST_APPROVAL,
    // Private scope
    INVITE_ONLY, TOKEN_BASED;

    public static RegistrationPolicy fromString(String string) {
        try {
            return RegistrationPolicy.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
