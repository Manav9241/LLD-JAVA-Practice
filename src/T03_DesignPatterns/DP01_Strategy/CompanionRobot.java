package T03_DesignPatterns.DP01_Strategy;

import T03_DesignPatterns.DP01_Strategy.FlyStrategy.IFlyableRobot;
import T03_DesignPatterns.DP01_Strategy.TalkStrategy.ITalkableRobot;
import T03_DesignPatterns.DP01_Strategy.WalkStrategy.IWalkableRobot;

public class CompanionRobot extends AbstractRobot{
    public CompanionRobot(IFlyableRobot flyBehaviour, ITalkableRobot talkBehaviour, IWalkableRobot walkBehaviour) {
        super(flyBehaviour, talkBehaviour, walkBehaviour);
    }

    @Override
    public void projection() {
        System.out.println("Displaying Companion Robot Efficiency Status");
    }
}
