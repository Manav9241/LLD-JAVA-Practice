package T01_OOPs.Polymorphism;

public abstract class Car {
    protected String brand;
    protected String model;
    protected boolean isEngineOn;
    protected int currentSpeed;

    public Car(String brand, String model){
        this.brand = brand;
        this.model = model;
        isEngineOn = false;
        currentSpeed = 0;
    }

    public void StartEngine(){
        isEngineOn = true;
        System.out.println(brand + " " + model + " : Engine Turned On");
    }

    public void StopEngine() {
        isEngineOn = false;
        currentSpeed = 0;
        System.out.println(brand + " " + model + " : Engine Turned Off");
    }

    public abstract void Accelerate();

    public abstract void Accelerate(int currentSpeed);

    public abstract void Brake();
}
