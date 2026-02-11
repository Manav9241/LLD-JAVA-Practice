package P02_TomatoFoodDeliveryApp.V02_BetterDesign.service;

import P02_TomatoFoodDeliveryApp.V02_BetterDesign.model.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantService {
    private final List<Restaurant> restaurants;

    public RestaurantService() {
        this.restaurants = new ArrayList<>();
    }

    public void addRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }
        restaurants.add(restaurant);
    }

    public Restaurant getRestaurantById(String id) {
        return restaurants.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Restaurant> searchByLocation(String location) {
        if (location == null || location.isEmpty()) {
            return new ArrayList<>();
        }
        return restaurants.stream()
                .filter(r -> r.getLocation().equalsIgnoreCase(location))
                .collect(Collectors.toList());
    }

    public List<Restaurant> getAllRestaurants() {
        return new ArrayList<>(restaurants);
    }
}
