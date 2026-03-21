package T03_DesignPatterns.DP06_Command.RemoteControlSmartAppliance.Command;

public interface ICommand {
    void execute();

    void undo();
}
