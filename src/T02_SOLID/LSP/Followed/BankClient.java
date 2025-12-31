package T02_SOLID.LSP.Followed;

import java.util.ArrayList;
import java.util.List;

public class BankClient {
    private List<IDepositOnlyAccount> depositOnlyAccountList;
    private List<IWithdrawableAccount> withdrawableAccountsList;

    public BankClient() {
        depositOnlyAccountList = new ArrayList<IDepositOnlyAccount>();
        withdrawableAccountsList = new ArrayList<IWithdrawableAccount>();
    }

    public void AddDepositOnlyAccount(IDepositOnlyAccount account) {
        depositOnlyAccountList.add(account);
    }

    public void AddWithdrawableAccount(IWithdrawableAccount account) {
        withdrawableAccountsList.add(account);
    }

    public void ProcessTransactions() {
        System.out.println("\nProcessing DepositOnlyAccounts...");
        for(IDepositOnlyAccount account: depositOnlyAccountList) {
            account.DepositMoney(1000.00);
            account.DepositMoney(500.00);
        }
        System.out.println("\nProcessing WithdrawableAccounts...");
        for(IWithdrawableAccount account: withdrawableAccountsList) {
            System.out.println(account.getClass().toString());
            account.DepositMoney(500.00);
            account.WithdrawMoney(1000.00);
            account.WithdrawMoney(200.00);
        }
    }
}
