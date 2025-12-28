package T01_OOPs.Inheritance;

public final class ManualCar extends Car{
    private int currentGear;

    public ManualCar(String brand, String model){
        super(brand, model);
        currentGear = 1;
    }

    public void ShiftGear(int gear){
        currentGear = gear;
        System.out.println(brand + " " + model + " : Shifted to Gear " + currentGear);
    }
}
