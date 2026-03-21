package T03_DesignPatterns.DP06_Command.BrokerageOrderExecutionEngine.Command;

public interface ICommand {
    void execute();

    void undo();

    String getAuditDescription();
}
