package T01_OOPs.Inheritance;

public final class ElectricCar extends Car{
    private int batteryLevel;

    public ElectricCar(String brand, String model){
        super(brand, model);
        batteryLevel = 10;
    }

    public void ChargeBattery() {
        batteryLevel += 10;
        System.out.println(brand + " " + model + " : Charging Battery to " + batteryLevel + "%");
    }
}
