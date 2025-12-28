package T01_OOPs.Inheritance;

public class Car {
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

    public void Accelerate() {
        if(!isEngineOn) {
            System.out.println(brand + " " + model + " : Cannot Accelerate, Engine is off");
        }
        currentSpeed += 20;
        System.out.println(brand + " " + model + " : Accelerated to : " + currentSpeed + " km/hr");
    }

    public void Brake(){
        currentSpeed = Math.max(0, currentSpeed-20);
        System.out.println(brand + " " + model + " : Braking! Speed is : "+ currentSpeed + "km/hr");
    }
}
