package T02_SOLID.LSP.Followed;

public class CurrentAccount implements IWithdrawableAccount{
    private double balance;

    public CurrentAccount() {
        balance = 100.0;
        System.out.println("CurrentAccount: Zero Minimum Balance Account for Frequent Transactions");
    }

    @Override
    public void DepositMoney(double money) {
        balance += money;
        System.out.println("CurrentAccount: Balance after Deposit: " + balance);
    }

    @Override
    public void WithdrawMoney(double money) {
        if(balance < money) {
            System.out.println("CurrentAccount: Insufficient Balance");
            return;
        }

        balance -= money;
        System.out.println("CurrentAccount: Balance after withdrawal: " + balance);
    }
}
