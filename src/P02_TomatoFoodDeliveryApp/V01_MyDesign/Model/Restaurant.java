package P02_TomatoFoodDeliveryApp.V01_MyDesign.Model;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private static int nextRestaurantID = 101;

    private final int id;
    private final String name;
    private final String address;
    private final List<MenuItem> menu;

    public Restaurant(String name, String address) {
        this.name = name;
        this.address = address;
        this.menu = new ArrayList<>();
        this.id = nextRestaurantID;
        nextRestaurantID += 1;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void addMenuItem(MenuItem menuItem) {
        menu.add(menuItem);
    }

    public List<MenuItem> getMenu() {
        return menu;
    }
}
