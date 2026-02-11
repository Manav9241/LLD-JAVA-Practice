package P02_TomatoFoodDeliveryApp.V01_MyDesign.Factory.OrderFactory;

import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Cart;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Order.DeliveryOrder;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Order.Order;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Order.PickupOrder;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Restaurant;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.User;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Utils.TimeUtils;

public class InstantOrderFactory implements FactoryOrder{
    @Override
    public Order createOrder(User user, String orderType) {
        Cart cart = user.getCart();
        Restaurant restaurant = cart.getRestaurant();

        Order order = null;
        if (orderType.equalsIgnoreCase("delivery")) {
            DeliveryOrder deliveryOrder = new DeliveryOrder();
            deliveryOrder.setUserAddress(user.getAddress());
            order = deliveryOrder;
        } else if (orderType.equalsIgnoreCase("pickup")) {
            PickupOrder pickupOrder = new PickupOrder();
            pickupOrder.setRestaurantAddress(restaurant.getAddress());
            order = pickupOrder;
        } else {
            System.out.println("Select correct Order Type!!!");
            return null;
        }

        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setItems(cart.getCartItems());
        order.setScheduledTime(TimeUtils.getCurrentTime());

        return order;
    }
}
