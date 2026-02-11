package P02_TomatoFoodDeliveryApp.V01_MyDesign.Manager;

import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Order.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private static OrderManager instance = null;
    private final List<Order> orders;

    private OrderManager() {
        orders = new ArrayList<>();
    }

    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void listOrders() {
        System.out.println("\n--- All Orders ---");
        for (Order order : orders) {
            System.out.println(order.getType() + " order for " + order.getUser().getName()
                    + " | Total: ₹" + order.getTotalAmount()
                    + " | At: " + order.getScheduledTime());
        }
    }
}
