package JavaPracticeProjects.PP01_BankAccountService.entities;

public class BankAccount {
    private final String accountId;
    private double balance;

    public BankAccount(String accountId, double openingBalance) {
        if(openingBalance < 0) {
            throw new IllegalArgumentException("Opening Balance cannot be negative");
        }

        this.accountId = accountId;
        this.balance = openingBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        validateAmount(amount);
        balance += amount;
    }

    public void withdraw(double amount) {
        validateAmount(amount);

        if(amount > balance) {
            throw new IllegalStateException("Insufficient Funds");
        }

        balance -= amount;
    }

    private void validateAmount(double amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount Cannot be negative");
        }
    }
}
