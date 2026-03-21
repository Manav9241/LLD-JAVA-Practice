package T03_DesignPatterns.DP06_Command.RemoteControlSmartAppliance.Command;

import T03_DesignPatterns.DP06_Command.RemoteControlSmartAppliance.Receiver.Fan;

public class FanCommand implements ICommand {
    private final Fan fan;

    public FanCommand(Fan fan) {
        this.fan = fan;
    }

    public void execute() {
        this.fan.start();
    }

    public void undo() {
        this.fan.stop();
    }
}
