package T03_DesignPatterns.DP06_Command.Command;

import T03_DesignPatterns.DP06_Command.Receiver.Light;

public class LightCommand implements ICommand{
    private final Light light;

    public LightCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        this.light.on();
    }

    public void undo() {
        this.light.off();
    }
}
