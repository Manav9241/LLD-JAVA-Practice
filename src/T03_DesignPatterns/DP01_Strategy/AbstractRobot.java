package T03_DesignPatterns.DP01_Strategy;

import T03_DesignPatterns.DP01_Strategy.FlyStrategy.IFlyableRobot;
import T03_DesignPatterns.DP01_Strategy.TalkStrategy.ITalkableRobot;
import T03_DesignPatterns.DP01_Strategy.WalkStrategy.IWalkableRobot;

public abstract class AbstractRobot {
    IFlyableRobot flyBehaviour;
    ITalkableRobot talkBehaviour;
    IWalkableRobot walkBehaviour;

    public AbstractRobot(IFlyableRobot flyBehaviour, ITalkableRobot talkBehaviour, IWalkableRobot walkBehaviour) {
        this.flyBehaviour = flyBehaviour;
        this.talkBehaviour = talkBehaviour;
        this.walkBehaviour = walkBehaviour;
    }

    public void fly() {
        flyBehaviour.fly();
    }

    public void talk() {
        talkBehaviour.talk();
    }

    public void walk() {
        walkBehaviour.walk();
    }

    public abstract void projection();
}
