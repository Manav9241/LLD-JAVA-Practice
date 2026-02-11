package T03_DesignPatterns.DP03_Singleton;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class S03_ThreadSafeEagerSingleton {
    private static S03_ThreadSafeEagerSingleton instance = new S03_ThreadSafeEagerSingleton();

    private S03_ThreadSafeEagerSingleton() {
        System.out.println("Object created");
    }

    public static S03_ThreadSafeEagerSingleton getInstance() {
        return instance;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Start");

        //region MultiThreading Test

        Set<Integer> instanceHashCodes = Collections.newSetFromMap(new ConcurrentHashMap<>());
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i=0; i < 100; i++) {
            executor.submit(() -> {
                S03_ThreadSafeEagerSingleton object = S03_ThreadSafeEagerSingleton.getInstance();
                instanceHashCodes.add(object.hashCode());
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Number of Unique Instances found: " + instanceHashCodes.size());
        if (instanceHashCodes.size() == 1) {
            System.out.println("Test Passed: Thread Safe Singleton.");
        } else {
            System.out.println("Test Failed: Multiple Instances Created!!");
        }

        //endregion

        System.out.println("End");
    }
}
