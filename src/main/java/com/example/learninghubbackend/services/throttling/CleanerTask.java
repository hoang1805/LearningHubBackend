package com.example.learninghubbackend.services.throttling;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class CleanerTask implements Task {
    private final BlockingQueue<Task> queue;
    private final AtomicBoolean flag;

    public CleanerTask(BlockingQueue<Task> queue, AtomicBoolean flag) {
        this.queue = queue;
        this.flag = flag;
    }


    @Override
    public void run() {
        try {
            queue.removeIf(task -> {
                if (task.isExpired()) {
                    task.onExpired();
                    return true;
                }

                return false;
            });
        } finally {
            // reset flag so we can enqueue cleaner again in the future
            flag.set(false);
        }
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public void onExpired() {

    }
}
