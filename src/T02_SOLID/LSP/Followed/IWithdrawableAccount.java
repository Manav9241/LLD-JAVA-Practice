package T02_SOLID.LSP.Followed;

public interface IWithdrawableAccount extends IDepositOnlyAccount{
    void WithdrawMoney(double money);
}
