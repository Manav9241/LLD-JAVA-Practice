package JavaPracticeProjects.PP02_ImmutableConfig.Mutable;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MutableApplicationClient {
    public static void main(String[] args) throws InterruptedException {
        MutableConfig config = new MutableConfig(new HashMap<>());
        config.set("tax", "0.10");

        MutablePricingService pricingService = new MutablePricingService(config);

        AtomicInteger countErrors = new AtomicInteger();
        Runnable reader = () -> {
            for (int i=0; i<100000; i++) {
                double price = pricingService.CalculatePrice(100);

                if (price != 110.0) {
                    countErrors.addAndGet(1);
                }
            }
            System.out.println(countErrors);
        };

        Runnable writer = () -> {
            for (int i = 0; i < 100; i++) {
                config.set("tax", "0.10");
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e.getMessage());
                }
                config.set("tax", "0.20");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        };

        Thread readerThread = new Thread(reader);
        Thread writerThread = new Thread(writer);

        readerThread.start();
        writerThread.start();

        readerThread.join();
        writerThread.join();

        System.out.println("Worker threads finished");
    }
}
