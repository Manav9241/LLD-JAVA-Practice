package T01_OOPs.Polymorphism;

public final class ManualCar extends Car {
    private int currentGear;

    public ManualCar(String brand, String model){
        super(brand, model);
        currentGear = 1;
    }

    @Override
    public void Accelerate() {
        if(!isEngineOn){
            System.out.println(brand + " " + model + " : Engine is Stationary! Cannot Accelerate");
            return;
        }
        if(currentSpeed == currentGear*15) {
            System.out.println(brand + " " + model + " : No Acceleration! Because No Gear Shift");
        }
        else {
            currentSpeed = currentGear*15;
            System.out.println(brand + " " + model + " : Accelerated to : " + currentSpeed + " km/hr");
        }
    }

    @Override
    public void Accelerate(int currentSpeed) {
        if(!isEngineOn){
            System.out.println(brand + " " + model + " : Engine is Stationary! Cannot Accelerate");
            return;
        }
        if(this.currentSpeed == currentSpeed) {
            System.out.println(brand + " " + model + " : No Acceleration! The Car Already at the given Speed");
        }
        else {
            this.currentSpeed = currentSpeed;
            this.currentGear = this.currentSpeed/15;
            System.out.println(brand + " " + model + " : Accelerated to : " + currentSpeed + " km/hr");
            System.out.println(brand + " " + model + " : Gear Shifted to  : " + currentGear);
        }
    }

    @Override
    public void Brake() {
        if(!isEngineOn) {
            System.out.println(brand + " " + model + " : Engine Stationary! No Brakes and no change in speed");
        }
        this.currentSpeed = Math.max(0, this.currentSpeed - 15);
        this.currentGear = this.currentSpeed/15;
        System.out.println(brand + " " + model + " : Brakes Applied! Speed is : " + currentSpeed + " km/hr");
        System.out.println(brand + " " + model + " : Gear Shifted to  : " + currentGear);
    }

    public void ShiftGear(int gear){
        currentGear = gear;
        System.out.println(brand + " " + model + " : Shifted to Gear " + currentGear);
    }
}
