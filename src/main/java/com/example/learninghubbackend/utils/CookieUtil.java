package com.example.learninghubbackend.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;

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

    public static Cookie generateCookie(@NonNull String key, @NonNull String value, @NonNull String path, @NonNull String maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath(path);
        cookie.setMaxAge(Math.toIntExact(TimerUtil.parseToSeconds(maxAge)));
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        return cookie;
    }
}
