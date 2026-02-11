package P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Order;

public class PickupOrder extends Order{
    private String restaurantAddress;

    @Override
    public String getType() {
        return "Pickup Order";
    }

    //region restaurantAddress
    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String address) {
        this.restaurantAddress = address;
    }
    //endregion
}
