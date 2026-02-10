package P02_TomatoFoodDeliveryApp.V01_MyDesign;

import P02_TomatoFoodDeliveryApp.V01_MyDesign.Manager.OrderManager;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Order.Order;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Restaurant;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.User;

import java.util.List;

public class TomatoMain {
    public static void main(String[] args) {
        User user = new User(101, "Manav", "Delhi");

        TomatoApp app = new TomatoApp(user);

        List<Restaurant> restaurants = app.searchByLocation();
        if (restaurants.isEmpty()) {
            System.out.println("No restaurants found");
            return;
        }

        Restaurant selectedRestaurant = restaurants.get(0);

        app.selectRestaurant(selectedRestaurant);

        System.out.println(selectedRestaurant.getMenu());

        app.addToCart("Q1");
        app.addToCart("Q1");
        app.addToCart("Q2");

        app.printUserCart();

        Order finalOrder = app.scheduledOrderCheckout("delivery", "upi", "9999999910", "Wed Feb 11 10:02:19 2026");

        app.payForOrder(finalOrder);

        System.out.println("______________________________________________________________");

        user.setAddress("Jaipur");

        List<Restaurant> jaipurRestaurants = app.searchByLocation();
        if (jaipurRestaurants.isEmpty()) {
            System.out.println("No restaurants found");
            return;
        }

        Restaurant res = jaipurRestaurants.get(0);

        app.selectRestaurant(res);

        System.out.println(res.getMenu());

        app.addToCart("P1");
        app.addToCart("P2");
        app.addToCart("P3");
        app.addToCart("P1");
        app.addToCart("P3");

        app.printUserCart();

        Order finalOrder2 = app.instantOrderCheckout("pickup", "card", "1212-XXXX-XXXX-0000");

        app.payForOrder(finalOrder2);

        System.out.println("_______________________________________________________");

        OrderManager.getInstance().listOrders();
    }
}
