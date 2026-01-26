package JavaPracticeProjects.Learning.ImmutableConfig.Immutable;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ImmutableApplicationClient {
    public static void main(String[] args) throws InterruptedException {
        HashMap<String, String> values = new HashMap<>();
        values.put("tax", "0.10");
        ImmutableConfig config = new ImmutableConfig(values);

        ImmutablePricingService service = new ImmutablePricingService(config);

        AtomicInteger countError = new AtomicInteger();

        Runnable reader = () -> {
            for (int i = 0; i < 1000; i++) {
                double price = service.CalculatePrice(100);

                if (price != 110.0) {
                    countError.addAndGet(1);
                }
            }
            System.out.println("✅ All prices correct, number of error in " + Thread.currentThread().getName() + " is " + countError);
        };

        Thread t1 = new Thread(reader);
        Thread t2 = new Thread(reader);
        Thread t3 = new Thread(reader);

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("GOOD DESIGN FINISHED");
    }
}
