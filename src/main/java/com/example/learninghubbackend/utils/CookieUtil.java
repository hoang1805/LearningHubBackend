package com.example.learninghubbackend.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CookieUtil {
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }

        return null;
    }

    public static Map<String, Cookie> toMap(HttpServletRequest request) {
        Cookie[] cookiesArray = request.getCookies();
        return Arrays.stream(cookiesArray).collect(Collectors.toMap(Cookie::getName, cookie -> cookie));
    }
}
