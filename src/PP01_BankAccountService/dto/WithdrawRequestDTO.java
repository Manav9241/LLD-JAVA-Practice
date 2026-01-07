package PP01_BankAccountService.dto;

public class WithdrawRequestDTO {
    private final String accountId;
    private final double amount;

    public WithdrawRequestDTO(String accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getAmount() {
        return amount;
    }
}
