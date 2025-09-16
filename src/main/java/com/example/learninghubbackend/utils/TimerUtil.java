package com.example.learninghubbackend.utils;

import org.springframework.boot.convert.DurationStyle;

import java.time.Duration;

public class TimerUtil {
    public static Long now() {
        return System.currentTimeMillis();
    }

    public static Duration parse(String time) {
        return DurationStyle.detectAndParse(time);
    }

    public static Long parseToSeconds(String time) {
        return parse(time).getSeconds();
    }
}
