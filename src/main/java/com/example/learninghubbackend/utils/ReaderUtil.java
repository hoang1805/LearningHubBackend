package com.example.learninghubbackend.utils;

public class ReaderUtil {
    public static String readString(String s) {
        if (s == null) {
            return "";
        }

        return s.trim();
    }

    public static boolean isValidString(String s, String regex) {
        if (s == null || s.isEmpty()) {
            return false;
        }

        if (regex == null || regex.isEmpty()) {
            return true;
        }

        return s.matches(regex);
    }

    public static boolean isValidString(String s) {
        return isValidString(s, null);
    }

    public static boolean isValidPassword(String password) {
        return isValidString(password, "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[_#?!@$%^&*-]).{8,}$");
    }

    public static boolean isValidEmail(String email) {
        return isValidString(email, "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public static boolean isValidUsername(String username) {
        return isValidString(username, "^[A-Za-z0-9_]+$");
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return isValidString(phoneNumber, "^(0[3|5|7|8|9])([0-9]{8})$");
    }
}
