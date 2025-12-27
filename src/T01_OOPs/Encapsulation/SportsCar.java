package T01_OOPs.Encapsulation;

public class SportsCar {
    private boolean isEngineOn;
    private int gear;
    private int currentSpeed;
    private String brand;
    private String model;

    public SportsCar(String brand, String model){
        isEngineOn = false;
        gear = 0;
        currentSpeed = 0;
        this.brand = brand;
        this.model = model;
    }

    // Getter method for brand and model
    public void GetCarName(){
        System.out.println(brand + " " + model);
    }

    public void StartEngine(){
        if(isEngineOn){
            System.out.println("Engine already running");
            return;
        }

        isEngineOn = true;
        System.out.println("Engine turned on");
    }

    public void ShiftGear(int gear){
        if(!isEngineOn){
            System.out.println("Engine is off, so no gear shift");
            return;
        }

        if(this.gear == gear){
            System.out.println("No Gear shift! Same Gear.");
            return;
        }

        if(gear == 0){
            if(currentSpeed == 0){
                this.gear = gear;
                System.out.println("Gear shifted to "+ gear);
                return;
            }
            if(currentSpeed > 0){
                System.out.println("No Gear Shift! Apply Brakes to Decelerate");
                return;
            }
        }

        if(this.currentSpeed < (gear-1)*15){
            System.out.println("No Gear Shift! Accelerate more");
            return;
        }

        if(this.currentSpeed > (gear*15)){
            System.out.println("No Gear Shift! Apply Brakes to Decelerate");
            return;
        }

        this.gear = gear;
        System.out.println("Gear Shifted to "+ gear);
    }

    public void Accelerate() {
        if(!isEngineOn){
            System.out.println("Cannot Accelerate a stationary Engine");
            return;
        }

        if(gear == 0){
            System.out.println("Cannot Accelerate when Gear is 0");
            return;
        }

        currentSpeed += 10;
        System.out.println("Accelerated to: "+ currentSpeed);
    }

    public void Brake(){
        if(!isEngineOn || currentSpeed == 0){
            System.out.println("No Brakes on stationary Engine");
        }

        currentSpeed = Math.max(0, currentSpeed - 10);
        System.out.println("Reduced Speed: "+ currentSpeed);
    }

    public void StopEngine(){
        if(!isEngineOn){
            System.out.println("Engine already Off");
            return;
        }

        isEngineOn = false;
        currentSpeed = 0;
        gear = 0;
        System.out.println("\nSpeed to 0");
        System.out.println("Gear on neutral");
        System.out.println("Engine Turned Off");
    }
}
