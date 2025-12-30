package T02_SOLID.LSP;

public class SavingsAccount implements IAccount{
    private double balance;

    public SavingsAccount () {
        balance = 0.00;
        System.out.println("SavingsAccount: Zero Minimum Balance Account");
    }

    @Override
    public void Deposit(double money) {
        balance += money;
        System.out.println("SavingsAccount: Balance after Deposit: " + balance);
    }

    @Override
    public void Withdraw(double money) {
        if(balance < money) {
            System.out.println("SavingsAccount: Insufficient balance");
            return;
        }

        balance -= money;
        System.out.println("SavingsAccount: Balance after Withdrawal: " + balance);
    }
}
