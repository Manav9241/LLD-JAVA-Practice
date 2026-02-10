package P02_TomatoFoodDeliveryApp.V01_MyDesign.Model.Order;

public class DeliveryOrder extends Order{
    private String userAddress;

    @Override
    public String getType() {
        return "Delivery Order";
    }

    //region userAddress
    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String address) {
        this.userAddress = address;
    }
    //endregion
}
