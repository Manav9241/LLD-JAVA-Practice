package T03_DesignPatterns.DP01_Strategy;

import T03_DesignPatterns.DP01_Strategy.FlyStrategy.NoFly;
import T03_DesignPatterns.DP01_Strategy.TalkStrategy.NormalTalk;
import T03_DesignPatterns.DP01_Strategy.WalkStrategy.NoWalk;

public class Main {
    public static void main(String[] args) {
        AbstractRobot robot = new CompanionRobot(
                new NoFly(),
                new NormalTalk(),
                new NoWalk()
        );

        robot.walk();
        robot.talk();
        robot.fly();

        robot.projection();
    }
}
