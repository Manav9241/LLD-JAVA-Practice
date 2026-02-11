package P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Order;

import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.MenuItem;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Restaurant;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.User;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Strategy.PaymentStrategy.IPaymentStrategy;

import java.util.ArrayList;
import java.util.List;

public abstract class Order {
    private static int nextOrderID = 0;

    private final int orderId;
    private User user;
    private Restaurant restaurant;
    private List<MenuItem> items;
    private double totalAmount;
    private IPaymentStrategy paymentStrategy;
    private String scheduledTime;

    public Order() {
        this.orderId = ++nextOrderID;
        this.user = null;
        this.restaurant = null;
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
        this.paymentStrategy = null;
        this.scheduledTime = "";
    }

    public abstract String getType();

    public boolean processPayment() {
        if (paymentStrategy == null) {
            System.out.println("Select Payment Strategy first!!!");
            return false;
        }
        if (totalAmount == 0.0 || items.isEmpty()) {
            System.out.println("Cannot process 0 amount payment");
            return false;
        }
        paymentStrategy.processPayment(totalAmount);
        return true;
    }

    //region orderId
    public int getOrderId() {
        return orderId;
    }
    //endregion

    //region user
    public User getUser() {
        return user;
    }

    public void setUser(User u) {
        this.user = u;
    }
    //endregion

    //region restaurant
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant res) {
        this.restaurant = res;
    }
    //endregion

    //region items
    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        // Make a defensive copy so later modifications to the source list
        // (for example, clearing a cart) do not affect this order's items.
        if (items == null) {
            this.items = new ArrayList<>();
        } else {
            this.items = new ArrayList<>(items);
        }
        this.totalAmount = 0;
        for (MenuItem it : this.items) {
            totalAmount += it.getPrice();
        }
    }
    //endregion

    //region totalAmount
    public double getTotalAmount() {
        return totalAmount;
    }
    //endregion

    //region paymentStrategy
    public void setPaymentStrategy(IPaymentStrategy ps) {
        this.paymentStrategy = ps;
    }
    //endregion

    //region scheduledTime
    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String time) {
        this.scheduledTime = time;
    }
    //endregion
}
