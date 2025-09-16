package com.example.learninghubbackend.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class HashUtil {
    private static final int SALT_LENGTH = 12;

    public static String hash(String s) {
        String salt = BCrypt.gensalt(SALT_LENGTH);
        return BCrypt.hashpw(s, salt);
    }

    public static boolean verify(String hash, String origin) {
        return BCrypt.checkpw(origin, hash);
    }
}
