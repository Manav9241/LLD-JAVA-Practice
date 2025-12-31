package T02_SOLID.LSP.Violated;

public class CurrentAccount implements IAccount{
    private double balance;

    public CurrentAccount() {
        balance = 100.0;
        System.out.println("CurrentAccount: Minimum Balance Rs100.00 added");
    }

    @Override
    public void Deposit(double money) {
        balance += money;
        System.out.println("CurrentAccount: Balance after Deposit: " + balance);
    }

    @Override
    public void Withdraw(double money) {
        if(balance < money) {
            System.out.println("CurrentAccount: Insufficient Balance");
            return;
        }

        balance -= money;
        System.out.println("CurrentAccount: Balance after withdrawal: " + balance);
    }
}
