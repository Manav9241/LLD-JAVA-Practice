package T03_DesignPatterns.DP01_Strategy.FlyStrategy;

public class NormalFly implements IFlyableRobot{
    @Override
    public void fly() {
        System.out.println("Normal Fly...");
    }
}
