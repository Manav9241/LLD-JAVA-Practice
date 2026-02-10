package P02_TomatoFoodDeliveryApp.V01_MyDesign.Factory.OrderFactory;

import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Order.Order;
import P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.User;

public interface FactoryOrder {
    Order createOrder(User user, String orderType);
}
