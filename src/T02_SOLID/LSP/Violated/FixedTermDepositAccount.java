package T02_SOLID.LSP.Violated;

public class FixedTermDepositAccount implements IAccount{
    private double principle;
    private int timeOfMaturity;

    public FixedTermDepositAccount(int months) {
        timeOfMaturity = months;
        System.out.println("FixedTermDepositAccount: Time Of Maturity Set to " + timeOfMaturity + " months");
    }

    @Override
    public void Deposit(double money) {
        if(principle > 0.00) {
            System.out.println("FixedTermDepositAccount: Balance already set");
            return;
        }

        principle += money;
        System.out.println("FixedTermDepositAccount: Principal Balance Set to: " + principle);
    }

    @Override
    public void Withdraw(double money) {
        throw new UnsupportedOperationException("FixedTermDepositAccount: Withdrawal of Principle NOT ALLOWED");
    }
}
