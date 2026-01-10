package PP03_OrderManagementSystem;

public class OrderApplicationMain {
    public static void main(String[] args) {
        System.out.println("Order Management System");

        OrderManager orderManager = new OrderManager();

        orderManager.CancelOrder("ORD1");
        orderManager.ShipOrder("ORD1");

        orderManager.CreateOrder("ORD1");
        orderManager.CreateOrder("ORD1");

        orderManager.CancelOrder("ORD1");
        orderManager.ShipOrder("ORD1");

        orderManager.CreateOrder("ORD2");
        orderManager.ShipOrder("ORD2");
        orderManager.CancelOrder("ORD2");
    }
}
