package P02_TomatoFoodDeliveryApp.V02_BetterDesign.model;

import java.util.*;

public class Cart {
    private Restaurant restaurant;
    private final Map<String, Integer> itemQuantities;
    private final Map<String, MenuItem> items;

    public Cart() {
        this.restaurant = null;
        this.itemQuantities = new HashMap<>();
        this.items = new HashMap<>();
    }

    public void setRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }
        if (this.restaurant != null && !this.restaurant.equals(restaurant)) {
            clearCart();
        }
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void addItem(MenuItem item) {
        if (restaurant == null) {
            throw new IllegalStateException("Restaurant must be set before adding items");
        }
        if (item == null) {
            throw new IllegalArgumentException("MenuItem cannot be null");
        }
        
        items.put(item.getId(), item);
        itemQuantities.put(item.getId(), itemQuantities.getOrDefault(item.getId(), 0) + 1);
    }

    public void removeItem(String itemId) {
        if (itemQuantities.containsKey(itemId)) {
            int quantity = itemQuantities.get(itemId);
            if (quantity > 1) {
                itemQuantities.put(itemId, quantity - 1);
            } else {
                itemQuantities.remove(itemId);
                items.remove(itemId);
            }
        }
    }

    public List<MenuItem> getItems() {
        List<MenuItem> cartItems = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            MenuItem item = items.get(entry.getKey());
            for (int i = 0; i < entry.getValue(); i++) {
                cartItems.add(item);
            }
        }
        return cartItems;
    }

    public Map<MenuItem, Integer> getItemsWithQuantity() {
        Map<MenuItem, Integer> result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            result.put(items.get(entry.getKey()), entry.getValue());
        }
        return result;
    }

    public double getTotalCost() {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            MenuItem item = items.get(entry.getKey());
            total += item.getPrice() * entry.getValue();
        }
        return total;
    }

    public boolean isEmpty() {
        return itemQuantities.isEmpty();
    }

    public void clearCart() {
        items.clear();
        itemQuantities.clear();
        restaurant = null;
    }

    public int getItemCount() {
        return itemQuantities.values().stream().mapToInt(Integer::intValue).sum();
    }
}
