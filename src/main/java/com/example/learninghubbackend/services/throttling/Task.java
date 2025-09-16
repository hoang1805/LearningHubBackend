package com.example.learninghubbackend.services.throttling;

public interface Task extends Runnable {
    boolean isExpired();
    void onExpired();
}
