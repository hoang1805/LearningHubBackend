package com.example.learninghubbackend.services.throttling;

import com.example.learninghubbackend.commons.PropertiesData;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ThrottlingService {
    private final Bucket bucket;
    private final BlockingQueue<Task> bucketQueue;
    private final AtomicBoolean cleanerEnqueued = new AtomicBoolean(false);
    private final ExecutorService worker = Executors.newSingleThreadExecutor();

    @Autowired
    public ThrottlingService(PropertiesData propertiesData) {
        int limit = propertiesData.getRateLimit();

        Bandwidth bandwidth = Bandwidth.builder().capacity(limit).refillGreedy(limit, Duration.ofSeconds(1)).build();
        this.bucket = Bucket.builder().addLimit(bandwidth).build();
        this.bucketQueue = new LinkedBlockingQueue<>(propertiesData.getMaxRateLimit());

        // Worker thread: dequeue và chạy task. Nếu task expired -> bỏ qua.
        worker.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Task task = bucketQueue.take(); // blocking
                    if (task.isExpired()) {
                        task.onExpired();
                        continue;
                    }
                    task.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    public CompletableFuture<Void> consume(Runnable runnable, Duration timeout) {
        Duration effectiveTimeout = timeout == null ? Duration.ZERO : timeout;
        TimedTask task = new TimedTask(runnable, effectiveTimeout);

        // chạy ngay nếu có token
        if (bucket.tryConsume(1)) {
            if (!task.isExpired()) {
                task.run();
            } else {
                task.onExpired();
            }
        } else {
            // cố gắng enqueue một CleanerTask chỉ 1 lần (tránh spam)
            if (cleanerEnqueued.compareAndSet(false, true)) {
                boolean accepted = bucketQueue.offer(new CleanerTask(bucketQueue, cleanerEnqueued));
                if (!accepted) {
                    // queue full -> reset flag để lần sau có thể thử enqueue lại
                    cleanerEnqueued.set(false);
                }
            }

            boolean accepted = bucketQueue.offer(task);
            if (!accepted) {
                task.onFull(); // báo fail ngay lập tức
            }

        }

        return task.getFuture();
    }
}
