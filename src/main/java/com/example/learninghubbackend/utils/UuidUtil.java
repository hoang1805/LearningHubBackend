package com.example.learninghubbackend.utils;

import com.fasterxml.uuid.Generators;

import java.util.UUID;

public class UuidUtil {
    public static UUID v7() {
        return Generators.timeBasedEpochRandomGenerator().generate();
    }

    public static UUID v4() {
        return Generators.randomBasedGenerator().generate();
    }

    public static UUID fromString(String str) {
        return UUID.fromString(str);
    }
}
