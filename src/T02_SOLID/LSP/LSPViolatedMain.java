package T02_SOLID.LSP;

public class LSPViolatedMain {
    static void main(String[] args) {
        BankingClient bankClient = new BankingClient();

        bankClient.AddAccount(new CurrentAccount());
        bankClient.AddAccount(new SavingsAccount());
        bankClient.AddAccount(new FixedTermDepositAccount(36));

        bankClient.ProcessTransactions();
    }
}
