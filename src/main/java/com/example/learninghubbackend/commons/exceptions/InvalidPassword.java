package com.example.learninghubbackend.commons.exceptions;

public class InvalidPassword extends InvalidField {
    private static final String RULE_MESSAGE =
            "Password must be at least 8 characters long, " +
                    "contain at least one uppercase letter, " +
                    "one lowercase letter, one number, " +
                    "and one special character (_#?!@$%^&*-).";

    public InvalidPassword() {
        super("password", RULE_MESSAGE);
    }
}
