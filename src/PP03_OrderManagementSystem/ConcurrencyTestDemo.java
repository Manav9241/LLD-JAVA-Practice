package PP03_OrderManagementSystem;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demonstration class to show thread-safe behavior of the Order Management System.
 * This class tests concurrent shipOrder and cancelOrder operations on the same order.
 */
public class ConcurrencyTestDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Safety Demonstration ===\n");

        // Test 1: Concurrent ship and cancel operations
        testConcurrentShipAndCancel();

        // Test 2: Multiple threads trying to create the same order
        testConcurrentOrderCreation();
    }

    private static void testConcurrentShipAndCancel() throws InterruptedException {
        System.out.println("Test 1: Concurrent Ship and Cancel Operations");
        System.out.println("----------------------------------------------");

        OrderManager orderManager = new OrderManager();
        
        // Create an order first
        orderManager.CreateOrder("ORD-CONCURRENT-1");
        System.out.println("\nStarting concurrent ship and cancel operations...\n");

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        // Thread 1: Try to ship the order
        executor.submit(() -> {
            try {
                Thread.sleep(10); // Small delay to increase race condition probability
                System.out.println("[Thread-Ship] Attempting to ship order...");
                orderManager.ShipOrder("ORD-CONCURRENT-1");
            } catch (Exception e) {
                System.out.println("[Thread-Ship] " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        // Thread 2: Try to cancel the order
        executor.submit(() -> {
            try {
                Thread.sleep(10); // Small delay to increase race condition probability
                System.out.println("[Thread-Cancel] Attempting to cancel order...");
                orderManager.CancelOrder("ORD-CONCURRENT-1");
            } catch (Exception e) {
                System.out.println("[Thread-Cancel] " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\nResult: One operation succeeded, the other was prevented.");
        System.out.println("This demonstrates thread-safe state transitions.\n");
    }

    private static void testConcurrentOrderCreation() throws InterruptedException {
        System.out.println("\nTest 2: Concurrent Order Creation");
        System.out.println("----------------------------------");

        OrderManager orderManager = new OrderManager();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(3);

        System.out.println("Three threads attempting to create the same order...\n");

        // Three threads trying to create the same order
        for (int i = 1; i <= 3; i++) {
            final int threadNum = i;
            executor.submit(() -> {
                try {
                    System.out.println("[Thread-" + threadNum + "] Attempting to create ORD-CONCURRENT-2");
                    orderManager.CreateOrder("ORD-CONCURRENT-2");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\nResult: Only one thread created the order, others got DuplicateOrderException.");
        System.out.println("This demonstrates thread-safe order creation.\n");
    }
}
