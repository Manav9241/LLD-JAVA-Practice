package T03_DesignPatterns.DP03_Singleton;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class S02_BasicSingletonMain {
    private static S02_BasicSingletonMain instance = null;

    private S02_BasicSingletonMain() {
        System.out.println("Object Created");
    }

    public static S02_BasicSingletonMain getInstance() {
        if (instance == null) {
            instance = new S02_BasicSingletonMain();
        }
        return instance;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Start");

//        S02_BasicSingletonMain object1 = S02_BasicSingletonMain.getInstance();
//        S02_BasicSingletonMain object2 = S02_BasicSingletonMain.getInstance();
//
//        System.out.println(object1 == object2);

        //region MultiThreading Test

        Set<Integer> instanceHashCodes = Collections.newSetFromMap(new ConcurrentHashMap<>());
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i=0; i < 100; i++) {
            executor.submit(() -> {
                S02_BasicSingletonMain object = S02_BasicSingletonMain.getInstance();
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
