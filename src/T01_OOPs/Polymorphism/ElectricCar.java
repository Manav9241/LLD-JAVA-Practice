package T01_OOPs.Polymorphism;

public final class ElectricCar extends Car {
    private int batteryLevel;

    public ElectricCar(String brand, String model){
        super(brand, model);
        batteryLevel = 10;
    }

    public void ChargeBattery() {
        batteryLevel += 10;
        System.out.println(brand + " " + model + " : Charging Battery to " + batteryLevel + "%");
    }

    @Override
    public void Accelerate() {
        if(!isEngineOn) {
            System.out.println(brand + " " + model + " : Cannot Accelerate, Engine is off");
            return;
        }
        currentSpeed += 20;
        batteryLevel = Math.max(0, batteryLevel-5);
        System.out.println(brand + " " + model + " : Accelerated to : " + currentSpeed + " km/hr");
        CheckBatteryLevel();
    }

    @Override
    public void Accelerate(int currentSpeed) {
        if(!isEngineOn) {
            System.out.println(brand + " " + model + " : Cannot Accelerate, Engine is off");
            return;
        }
        if(currentSpeed >= this.currentSpeed) {
            this.currentSpeed = currentSpeed;
            batteryLevel = Math.max(0, batteryLevel-5);
            System.out.println(brand + " " + model + " : Accelerated to : " + currentSpeed + " km/hr");
            CheckBatteryLevel();
        }
        else {
            System.out.println(brand + " " + model + " : Acceleration did not happen, speed entered is less than currentSpeed");
        }
    }

    @Override
    public void Brake() {
        if(!isEngineOn) {
            System.out.println(brand + " " + model + " : No Braking! Engine is off");
            return;
        }
        currentSpeed = Math.max(0, currentSpeed-20);
        batteryLevel = Math.max(0, batteryLevel-2);
        System.out.println(brand + " " + model + " : Brakes Applied! New Speed : " + currentSpeed + " km/hr");
        CheckBatteryLevel();
    }

    private void CheckBatteryLevel(){
        if(batteryLevel == 0){
            System.out.println(brand + " " + model + " : OutOfBattery");
            if(isEngineOn){
                this.StopEngine();
            }
            return;
        }
        System.out.println(brand + " " + model + " : Battery Level : " + batteryLevel + "%");
    }
}
