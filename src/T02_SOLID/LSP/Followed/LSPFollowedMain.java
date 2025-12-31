package T02_SOLID.LSP.Followed;

public class LSPFollowedMain {
    static void main(String[] args) {
        BankClient client = new BankClient();

        client.AddDepositOnlyAccount(new FixedTermDepositAccount(36));
        client.AddWithdrawableAccount(new SavingsAccount());
        client.AddWithdrawableAccount(new CurrentAccount());

        client.ProcessTransactions();
    }
}
