package P02_TomatoFoodDeliveryApp.V01_MyDesign;

import P02_TomatoFoodDeliveryApp.V01_MyDesign.Factory.FactoryPaymentStrategy;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Factory.OrderFactory.FactoryOrder;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Factory.OrderFactory.InstantOrderFactory;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Factory.OrderFactory.ScheduledOrderFactory;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Manager.OrderManager;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Manager.RestaurantManager;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.MenuItem;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Order.Order;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Restaurant;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.User;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Strategy.NotificationService.NotificationService;

import java.util.List;

public class TomatoApp {
    private final User user;

    public TomatoApp(User user) {
        initializeRestaurants();
        this.user = user;
        System.out.println("User: " + user.getName() + " is active.");
    }

    public void initializeRestaurants() {
        RestaurantManager restaurantManager = RestaurantManager.getInstance();

        Restaurant r1 = new Restaurant("Bikaji", "Jaipur");
        r1.addMenuItem(new MenuItem("P1", "Chole Bhature", 120));
        r1.addMenuItem(new MenuItem("P2", "Chole Kulche", 140));
        r1.addMenuItem(new MenuItem("P3", "Samosa", 40));

        Restaurant r2 = new Restaurant("Haldirams", "Delhi");
        r2.addMenuItem(new MenuItem("Q1", "Aloo Chaat", 100));
        r2.addMenuItem(new MenuItem("Q2", "Golgappe", 60));

        Restaurant r3 = new Restaurant("Asha Tiffins", "Bangalore");
        r3.addMenuItem(new MenuItem("R1", "Dosa", 60));
        r3.addMenuItem(new MenuItem("R2", "Idli", 30));
        r3.addMenuItem(new MenuItem("R3", "South Meal", 120));

        restaurantManager.addRestaurant(r1);
        restaurantManager.addRestaurant(r2);
        restaurantManager.addRestaurant(r3);
    }

    public List<Restaurant> searchByLocation() {
        return RestaurantManager.getInstance().searchByLocation(user.getAddress());
    }

    public void selectRestaurant(Restaurant r) {
        user.getCart().setRestaurant(r);
    }

    public void addToCart(String itemCode) {
        Restaurant res = user.getCart().getRestaurant();
        if (res == null) {
            System.out.println("Select a restaurant first!!!");
            return;
        }

        for (MenuItem item: res.getMenu()) {
            if(item.getCode().equalsIgnoreCase(itemCode)) {
                user.getCart().addToCart(item);
            }
        }
    }

    public Order instantOrderCheckout(String orderType, String paymentMode, String accountDetails) {
        return checkout(orderType, paymentMode, accountDetails, new InstantOrderFactory());
    }

    public Order scheduledOrderCheckout(String orderType, String paymentMode, String accountDetails, String scheduledTime) {
        return checkout(orderType, paymentMode, accountDetails, new ScheduledOrderFactory(scheduledTime));
    }

    private Order checkout(String orderType, String paymentMode, String accountDetails, FactoryOrder orderFactory) {
        if (user.getCart().isEmpty()) return null;

        Order order = orderFactory.createOrder(user, orderType);
        order.setPaymentStrategy(
                FactoryPaymentStrategy
                        .getInstance()
                        .createPaymentStrategyObject(paymentMode, accountDetails)
        );

        OrderManager.getInstance().addOrder(order);

        return order;
    }

    public void payForOrder(Order order) {
        if (order.processPayment()) {
            NotificationService.notify(order);
            user.getCart().clearCart();
        }
    }

    public void printUserCart() {
        System.out.println("Items in cart:");
        System.out.println("------------------------------------");
        for (MenuItem item : user.getCart().getCartItems()) {
            System.out.println(item.getCode() + " : " + item.getName() + " : ₹" + item.getPrice());
        }
        System.out.println("------------------------------------");
        System.out.println("Grand total : ₹" + user.getCart().getTotalCost());
    }
}
