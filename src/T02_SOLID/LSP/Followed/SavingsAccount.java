package T02_SOLID.LSP.Followed;

public class SavingsAccount implements IWithdrawableAccount{
    private double balance;

    public SavingsAccount () {
        balance = 100.00;
        System.out.println("SavingsAccount: Minimum Balance Rs.100 Deposited");
    }

    @Override
    public void DepositMoney(double money) {
        balance += money;
        System.out.println("SavingsAccount: Balance after Deposit: " + balance);
    }

    @Override
    public void WithdrawMoney(double money) {
        if(balance < money) {
            System.out.println("SavingsAccount: Insufficient balance");
            return;
        }

        balance -= money;
        System.out.println("SavingsAccount: Balance after Withdrawal: " + balance);
    }
}
