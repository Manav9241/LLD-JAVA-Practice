package T01_OOPs.Inheritance;

public class InheritanceMain {
    static void main(String[] args) {
        ManualCar myManualCar = new ManualCar("Suzuki", "Swift");
        myManualCar.StartEngine();
        myManualCar.Accelerate();
        myManualCar.Accelerate();
        myManualCar.ShiftGear(2);
        myManualCar.Brake();
        myManualCar.StopEngine();

        System.out.println("-----------------------------------");

        ElectricCar myElectricCar = new ElectricCar("Tata", "Nexon");
        myElectricCar.StartEngine();
        myElectricCar.Accelerate();
        myElectricCar.ChargeBattery();
        myElectricCar.Accelerate();
        myElectricCar.Brake();
        myElectricCar.StopEngine();
    }
}
