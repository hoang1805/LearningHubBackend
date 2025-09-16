package com.example.learninghubbackend.commons.models;

public interface Releasable<T, C> {
    T release();
    C releaseCompact();
}
