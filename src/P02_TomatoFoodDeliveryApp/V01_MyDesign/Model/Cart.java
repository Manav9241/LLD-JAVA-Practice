package P02_TomatoFoodDeliveryApp.V01_MyDesign.Model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private Restaurant restaurant;
    private final List<MenuItem> items;

    public Cart() {
        this.restaurant = null;
        items = new ArrayList<>();
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void addToCart(MenuItem item) {
        if (restaurant == null) {
            System.out.println("Set Restaurant value before adding item");
            return;
        }
        items.add(item);
    }

    public List<MenuItem> getCartItems() {
        return items;
    }

    public double getTotalCost() {
        double total = 0;
        for (MenuItem item: items) {
            total += item.getPrice();
        }
        return total;
    }

    public boolean isEmpty() {
        return restaurant == null || items.isEmpty();
    }

    public void clearCart() {
        items.clear();
        restaurant = null;
    }
}
