package T03_DesignPatterns.DP01_Strategy.WalkStrategy;

public class NoWalk implements IWalkableRobot{
    @Override
    public void walk() {
        System.out.println("Cannot Walk!!");
    }
}
