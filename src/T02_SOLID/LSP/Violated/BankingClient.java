package T02_SOLID.LSP.Violated;

import java.util.ArrayList;
import java.util.List;

public class BankingClient {
    private List<IAccount> accounts;

    public BankingClient() {
        this.accounts = new ArrayList<IAccount>();
    }

    public void AddAccount(IAccount account) {
        this.accounts.add(account);
    }

    public void ProcessTransactions() {
        for(IAccount account: this.accounts) {
            try{
                account.Deposit(500.00);
                account.Withdraw(1000.00);
                account.Withdraw(350.00);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
