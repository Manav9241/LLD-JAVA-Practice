package PP01_BankAccountService.dto;

public class DepositRequestDTO {
    private final String accountId;
    private final double amount;

    public DepositRequestDTO(String accountId, double amount) {
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
