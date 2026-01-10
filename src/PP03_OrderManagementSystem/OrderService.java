package PP03_OrderManagementSystem;

public class OrderService {
    private OrderRepository repository;

    public OrderService() {
        repository = new OrderRepository();
    }

    public void CreateOrder(String orderId) {
        if (repository.findOrderById(orderId) != null) {
            System.out.println("Duplicate Order: Order Already Exists");
            return;
        }

        Order newOrder = new Order(orderId);
        repository.saveToDB(newOrder);
        System.out.println("Order Created");
    }

    public void CancelOrder(String orderId) {
        Order order = repository.findOrderById(orderId);
        if (order == null) {
            System.out.println("Invalid Id: Order Not Found");
            return;
        }
        if (order.getStatus().equals("SHIPPED")) {
            System.out.println("Invalid State: Shipped Order Cannot be Cancelled");
            return;
        }
        order.cancel();
        repository.saveToDB(order);
        System.out.println("Order Cancelled");
    }

    public void ShipOrder(String orderId) {
        Order order = repository.findOrderById(orderId);
        if (order == null) {
            System.out.println("Invalid Id: Order Not Found");
            return;
        }
        if (order.getStatus().equals("CANCELLED")) {
            System.out.println("Invalid State: Cancelled Order Cannot be Shipped");
            return;
        }
        order.ship();
        repository.saveToDB(order);
        System.out.println("Order Shipped");
    }
}
