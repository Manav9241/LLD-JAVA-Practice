package T03_DesignPatterns.DP01_Strategy.TalkStrategy;

public class NormalTalk implements ITalkableRobot{
    @Override
    public void talk() {
        System.out.println("Talking normally...");
    }
}
