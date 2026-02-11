package P02_TomatoFoodDeliveryApp.V02_BetterDesign;

import P02_TomatoFoodDeliveryApp.V02_BetterDesign.enums.OrderType;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.enums.PaymentMethod;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.model.*;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.service.RestaurantService;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.strategy.EmailNotificationStrategy;
import P02_TomatoFoodDeliveryApp.V02_BetterDesign.strategy.SMSNotificationStrategy;

import java.time.LocalDateTime;
import java.util.List;

public class TomatoMain {
    public static void main(String[] args) {
        // Initialize the app
        TomatoFoodDeliveryApp app = new TomatoFoodDeliveryApp();
        
        // Add notification strategies
        app.getNotificationService().addNotificationStrategy(new EmailNotificationStrategy());
        app.getNotificationService().addNotificationStrategy(new SMSNotificationStrategy());
        
        // Initialize restaurants
        initializeRestaurants(app.getRestaurantService());
        
        // Create a user
        User user = new User("U101", "Manav", "Delhi");
        System.out.println("User " + user.getName() + " is active in " + user.getLocation());
        
        // Scenario 1: Order from Delhi
        System.out.println("\n========== SCENARIO 1: Order from Delhi ==========");
        
        // Search restaurants in Delhi
        List<Restaurant> delhiRestaurants = app.searchRestaurantsByLocation(user.getLocation());
        if (!delhiRestaurants.isEmpty()) {
            Restaurant haldirams = delhiRestaurants.get(0);
            System.out.println("Found restaurant: " + haldirams.getName());
            
            // Display menu
            System.out.println("\nMenu:");
            for (MenuItem item : haldirams.getMenu()) {
                System.out.println("  " + item);
            }
            
            // Add items to cart
            app.addItemToCart(user, haldirams, "Q1");
            app.addItemToCart(user, haldirams, "Q1");
            app.addItemToCart(user, haldirams, "Q2");
            
            // Display cart
            app.displayCart(user);
            
            // Create and pay for scheduled delivery order
            LocalDateTime scheduledTime = LocalDateTime.now().plusHours(2);
            Order order1 = app.createOrder(user, OrderType.DELIVERY, scheduledTime);
            app.processOrderPayment(order1, PaymentMethod.UPI, "9999999910");
            
            // Clear cart after successful order
            user.getCart().clearCart();
        }
        
        // Scenario 2: Order from Jaipur
        System.out.println("\n========== SCENARIO 2: Order from Jaipur ==========");
        
        // User changes location
        user.setLocation("Jaipur");
        System.out.println("User location changed to: " + user.getLocation());
        
        // Search restaurants in Jaipur
        List<Restaurant> jaipurRestaurants = app.searchRestaurantsByLocation(user.getLocation());
        if (!jaipurRestaurants.isEmpty()) {
            Restaurant bikaji = jaipurRestaurants.get(0);
            System.out.println("Found restaurant: " + bikaji.getName());
            
            // Display menu
            System.out.println("\nMenu:");
            for (MenuItem item : bikaji.getMenu()) {
                System.out.println("  " + item);
            }
            
            // Add items to cart
            app.addItemToCart(user, bikaji, "P1");
            app.addItemToCart(user, bikaji, "P2");
            app.addItemToCart(user, bikaji, "P3");
            app.addItemToCart(user, bikaji, "P1");
            app.addItemToCart(user, bikaji, "P3");
            
            // Display cart
            app.displayCart(user);
            
            // Create and pay for instant pickup order
            Order order2 = app.createOrder(user, OrderType.PICKUP, null);
            app.processOrderPayment(order2, PaymentMethod.CARD, "1212-XXXX-XXXX-0000");
            
            // Clear cart after successful order
            user.getCart().clearCart();
        }
        
        // Display all orders
        System.out.println("\n========== ALL ORDERS ==========");
        app.displayOrders();
    }
    
    private static void initializeRestaurants(RestaurantService restaurantService) {
        // Restaurant 1: Bikaji in Jaipur
        Restaurant bikaji = new Restaurant("R101", "Bikaji", "Jaipur");
        bikaji.addMenuItem(new MenuItem("P1", "Chole Bhature", 120, "Spicy chickpeas with fried bread"));
        bikaji.addMenuItem(new MenuItem("P2", "Chole Kulche", 140, "Chickpeas with soft bread"));
        bikaji.addMenuItem(new MenuItem("P3", "Samosa", 40, "Fried pastry with savory filling"));
        restaurantService.addRestaurant(bikaji);
        
        // Restaurant 2: Haldirams in Delhi
        Restaurant haldirams = new Restaurant("R102", "Haldirams", "Delhi");
        haldirams.addMenuItem(new MenuItem("Q1", "Aloo Chaat", 100, "Spiced potato snack"));
        haldirams.addMenuItem(new MenuItem("Q2", "Golgappe", 60, "Crispy water balls"));
        restaurantService.addRestaurant(haldirams);
        
        // Restaurant 3: Asha Tiffins in Bangalore
        Restaurant ashaTiffins = new Restaurant("R103", "Asha Tiffins", "Bangalore");
        ashaTiffins.addMenuItem(new MenuItem("R1", "Dosa", 60, "Crispy rice crepe"));
        ashaTiffins.addMenuItem(new MenuItem("R2", "Idli", 30, "Steamed rice cakes"));
        ashaTiffins.addMenuItem(new MenuItem("R3", "South Meal", 120, "Complete south Indian thali"));
        restaurantService.addRestaurant(ashaTiffins);
    }
}
