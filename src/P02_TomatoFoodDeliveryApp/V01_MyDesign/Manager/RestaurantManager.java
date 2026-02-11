package P02_TomatoFoodDeliveryApp.V01_MyDesign.Manager;

import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantManager {
    private static RestaurantManager instance = null;

    private final List<Restaurant> restaurants;
    private boolean initialized = false;

    private RestaurantManager() {
        this.restaurants = new ArrayList<>();
    }

    public static RestaurantManager getInstance() {
        if (instance == null) {
            instance = new RestaurantManager();
        }
        return instance;
    }

    public void addRestaurant(Restaurant res) {
        restaurants.add(res);
    }

    public List<Restaurant> SearchByLocation(String location) {
        List<Restaurant> result = new ArrayList<>();
        for (Restaurant r: restaurants) {
            if (r.getAddress().equalsIgnoreCase(location)) {
                result.add(r);
            }
        }
        return result;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void markAsInitialized() {
        initialized = true;
    }
}
