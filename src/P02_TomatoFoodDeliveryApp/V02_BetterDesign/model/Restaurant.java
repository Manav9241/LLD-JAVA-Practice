package P02_TomatoFoodDeliveryApp.V02_BetterDesign.model;

import java.util.*;

public class Restaurant {
    private final String id;
    private final String name;
    private final String location;
    private final Map<String, MenuItem> menu;

    public Restaurant(String id, String name, String location) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Restaurant ID cannot be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Restaurant name cannot be null or empty");
        }
        if (location == null || location.isEmpty()) {
            throw new IllegalArgumentException("Restaurant location cannot be null or empty");
        }
        this.id = id;
        this.name = name;
        this.location = location;
        this.menu = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public void addMenuItem(MenuItem item) {
        if (item == null) {
            throw new IllegalArgumentException("MenuItem cannot be null");
        }
        menu.put(item.getId(), item);
    }

    public MenuItem getMenuItem(String itemId) {
        return menu.get(itemId);
    }

    public List<MenuItem> getMenu() {
        return new ArrayList<>(menu.values());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
