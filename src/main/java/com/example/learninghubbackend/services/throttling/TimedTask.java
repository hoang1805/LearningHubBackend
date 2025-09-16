package com.example.learninghubbackend.services.throttling;

import com.example.learninghubbackend.commons.exceptions.TimeoutRequest;
import com.example.learninghubbackend.commons.exceptions.TooManyRequests;
import lombok.Getter;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class TimedTask implements Task {
    private final Runnable runnable;
    @Getter
    private final CompletableFuture<Void> future = new CompletableFuture<>();
    private final long expireAt;

    public TimedTask(Runnable runnable, Duration timeout) {
        this.runnable = runnable;

        if (timeout == null || timeout.isZero() || timeout.isNegative()) {
            this.expireAt = Long.MAX_VALUE;
        } else {
            this.expireAt = System.currentTimeMillis() + timeout.toMillis();
        }
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expireAt;
    }

    @Override
    public void onExpired() {
        future.completeExceptionally(new TimeoutRequest());
    }

    public void onFull() {
        future.completeExceptionally(new TooManyRequests());
    }

    public void run() {
        try {
            runnable.run();
            future.complete(null);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
    }

}
