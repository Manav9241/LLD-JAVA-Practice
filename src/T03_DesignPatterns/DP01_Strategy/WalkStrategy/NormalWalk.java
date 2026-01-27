package T03_DesignPatterns.DP01_Strategy.WalkStrategy;

public class NormalWalk implements IWalkableRobot{
    @Override
    public void walk() {
        System.out.println("Walking normally...");
    }
}
