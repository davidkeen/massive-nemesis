package com.davidkeen;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class TestHarness {
    public long timeTasks(int nThreads, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread("Thread " + i) {
                public void run() {
                    try {
                        startGate.await();
                        try {
                            System.out.println("Thread start: " + getName());
                            task.run();
                        } finally {
                            System.out.println("Thread finish: " + getName());
                            endGate.countDown();
                        }
                    } catch (InterruptedException ignored) { }
                }
            };
            System.out.println("Created thread: " + t.getName());
            t.start();
        }

        long start = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long end = System.nanoTime();
        return end - start;
    }

    public static void main(String[] args) {
        TestHarness testHarness = new TestHarness();
        long time = 0;
        try {
            time = testHarness.timeTasks(5, () -> {
                for (int i = 0; i < 5; i++) {
                    try {
                        Random random = new Random();
                        long workTime = random.nextInt(3) * 1000;
                        System.out.println(Thread.currentThread().getName() + " working for " + workTime + " ms");
                        Thread.sleep(workTime);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(time);
    }
}
