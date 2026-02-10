package P02_TomatoFoodDeliveryApp.V01_MyDesign.Model;

public class User {
    private final int userId;
    private final String name;
    private String address;
    private final Cart cart;

    public User(int userId, String name, String address) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.cart = new Cart();
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Cart getCart() {
        return cart;
    }
}
