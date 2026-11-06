package com.minibank.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class TransactionProcessor {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final List<Throwable> failures = new ArrayList<>();

    public void submit(Runnable task) {
        executor.execute(() -> {
            try {
                task.run();
            } catch (Throwable failure) {
                synchronized (failures) {
                    failures.add(failure);
                }
            }
        });
    }

    public void stop() throws InterruptedException {
        executor.shutdown();
        if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
            executor.shutdownNow();
            throw new IllegalStateException("Transaction processor did not stop");
        }
        synchronized (failures) {
            if (!failures.isEmpty()) {
                throw new IllegalStateException("A transaction failed", failures.get(0));
            }
        }
    }
}
