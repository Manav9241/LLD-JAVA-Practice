package T03_DesignPatterns.DP06_Command;

import T03_DesignPatterns.DP06_Command.Command.FanCommand;
import T03_DesignPatterns.DP06_Command.Command.LightCommand;
import T03_DesignPatterns.DP06_Command.Receiver.Fan;
import T03_DesignPatterns.DP06_Command.Receiver.Light;

public class CommandPatternMain {
    public static void main(String[] args) {
        Light livingRoomLight = new Light();
        Fan ceilingFan = new Fan();

        RemoteControlInvoker remote = new RemoteControlInvoker();

        remote.setButton(0, new LightCommand(livingRoomLight));
        remote.setButton(1, new FanCommand(ceilingFan));

        remote.pressButton(0);
        remote.pressButton(0);

        remote.pressButton(1);
        remote.pressButton(1);

        remote.pressButton(2);
    }
}
