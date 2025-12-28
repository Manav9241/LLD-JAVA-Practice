package T01_OOPs.Polymorphism;

public class PolymorphismMain {
    static void main(String[] args) {
        ManualCar myManualCar = new ManualCar("Suzuki", "Swift");
        myManualCar.StartEngine();
        myManualCar.Accelerate();
        myManualCar.Accelerate();
        myManualCar.ShiftGear(2);
        myManualCar.Accelerate();
        myManualCar.Accelerate(85);
        myManualCar.Brake();
        myManualCar.Brake();
        myManualCar.StopEngine();

        System.out.println("-----------------------------------");

        ElectricCar myElectricCar = new ElectricCar("Tata", "Nexon");
        myElectricCar.StartEngine();
        myElectricCar.Accelerate();
        myElectricCar.Accelerate();
        myElectricCar.ChargeBattery();
        myElectricCar.StartEngine();
        myElectricCar.Accelerate();
        myElectricCar.ChargeBattery();
        myElectricCar.Accelerate();
        myElectricCar.Accelerate();
        myElectricCar.Brake();
        myElectricCar.StopEngine();
    }
}
