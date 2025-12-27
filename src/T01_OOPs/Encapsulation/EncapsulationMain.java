package T01_OOPs.Encapsulation;

public class EncapsulationMain {
    static void main(String[] args) {
        SportsCar car = new SportsCar("Mahindra", "Thar");

//        car.model = "XUV500";     // Cannot access this because of private access specifier
        car.GetCarName();           // Hence Getter method is defined to get the car name and there is no setter for these variables
        car.StopEngine();
        car.StartEngine();
        car.Accelerate();
        car.ShiftGear(0);
        car.ShiftGear(2);
        car.ShiftGear(1);
        car.Accelerate();
        car.Accelerate();
        car.ShiftGear(2);
        car.Accelerate();
        car.Accelerate();
        car.ShiftGear(1);
        car.ShiftGear(3);
        car.Brake();
        car.Brake();
        car.Brake();
        car.Brake();
        car.ShiftGear(1);
        car.StopEngine();
    }
}
