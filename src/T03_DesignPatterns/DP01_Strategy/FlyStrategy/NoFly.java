package T03_DesignPatterns.DP01_Strategy.FlyStrategy;

public class NoFly implements IFlyableRobot{
    @Override
    public void fly() {
        System.out.println("Cannot Fly!!");
    }
}
