package T03_DesignPatterns.DP01_Strategy.TalkStrategy;

public class NoTalk implements ITalkableRobot{
    @Override
    public void talk() {
        System.out.println("Cannot Talk!!");
    }
}
