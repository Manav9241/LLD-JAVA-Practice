package P02_TomatoFoodDeliveryApp.V02_BetterDesign.model;

import java.util.Objects;

public class MenuItem {
    private final String id;
    private final String name;
    private final double price;
    private final String description;

    public MenuItem(String id, String name, double price, String description) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("MenuItem ID cannot be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("MenuItem name cannot be null or empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("MenuItem price cannot be negative");
        }
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return Objects.equals(id, menuItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + " : " + name + " : ₹" + price;
    }
}
