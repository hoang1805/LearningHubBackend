package com.example.learninghubbackend.commons;

public interface Releasable<T, C> {
    T release();
    C releaseCompact();
}
