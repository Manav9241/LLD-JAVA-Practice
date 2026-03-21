package T03_DesignPatterns.DP06_Command.RemoteControlSmartAppliance;

import T03_DesignPatterns.DP06_Command.RemoteControlSmartAppliance.Command.FanCommand;
import T03_DesignPatterns.DP06_Command.RemoteControlSmartAppliance.Command.LightCommand;
import T03_DesignPatterns.DP06_Command.RemoteControlSmartAppliance.Receiver.Fan;
import T03_DesignPatterns.DP06_Command.RemoteControlSmartAppliance.Receiver.Light;

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
