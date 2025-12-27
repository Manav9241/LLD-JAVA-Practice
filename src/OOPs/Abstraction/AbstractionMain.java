package OOPs.Abstraction;

public class AbstractionMain {
    static void main(String[] args) {
        ICar car = new SportsCar();

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
