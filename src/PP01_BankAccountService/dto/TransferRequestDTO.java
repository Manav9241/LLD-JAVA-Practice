package PP01_BankAccountService.dto;

public class TransferRequestDTO {
    private final String fromAccountId;
    private final String toAccountId;
    private final double amount;

    public TransferRequestDTO(String fromAccountId, String toAccountId, double amount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public double getAmount() {
        return amount;
    }
}
