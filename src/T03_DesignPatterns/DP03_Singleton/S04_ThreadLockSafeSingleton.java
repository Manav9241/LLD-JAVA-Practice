package T03_DesignPatterns.DP03_Singleton;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.*;

public class S04_ThreadLockSafeSingleton {
    private static S04_ThreadLockSafeSingleton instance = null;

    private S04_ThreadLockSafeSingleton() {
        System.out.println("Object Created");
    }

    public static S04_ThreadLockSafeSingleton getInstance() {
        synchronized (S04_ThreadLockSafeSingleton.class) {
            if (instance == null) {
                instance = new S04_ThreadLockSafeSingleton();
            }
            return instance;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //region MultiThreading Test

//        Set<Integer> instanceHashCodes = Collections.newSetFromMap(new ConcurrentHashMap<>());
//        ExecutorService executor = Executors.newFixedThreadPool(10);
//
//        for (int i=0; i < 100; i++) {
//            executor.submit(() -> {
//                S04_ThreadLockSafeSingleton object = S04_ThreadLockSafeSingleton.getInstance();
//                instanceHashCodes.add(object.hashCode());
//            });
//        }
//
//        executor.shutdown();
//        executor.awaitTermination(5, TimeUnit.SECONDS);
//
//        System.out.println("Number of Unique Instances found: " + instanceHashCodes.size());
//        if (instanceHashCodes.size() == 1) {
//            System.out.println("Test Passed: Thread Safe Singleton.");
//        } else {
//            System.out.println("Test Failed: Multiple Instances Created!!");
//        }

        int threadCount = 100;
        int iterations = 1000000; // 1 Million calls per thread
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);

        Runnable task = () -> {
            try {
                latch.await(); // Wait for all threads to be ready
                for (int i = 0; i < iterations; i++) {
                    S04_ThreadLockSafeSingleton.getInstance();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < threadCount; i++) executor.submit(task);

        long start = System.currentTimeMillis();
        latch.countDown(); // Start all threads at once
        executor.shutdown();
        executor.awaitTermination(1, java.util.concurrent.TimeUnit.MINUTES);
        long end = System.currentTimeMillis();

        System.out.println("Total Time with basic locking: " + (end - start) + "ms");

        //endregion
    }
}
