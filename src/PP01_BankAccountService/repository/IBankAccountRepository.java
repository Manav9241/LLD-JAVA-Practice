package PP01_BankAccountService.repository;

import PP01_BankAccountService.entities.BankAccount;

public interface IBankAccountRepository {
    BankAccount getAccountById(String accountId);
    void save(BankAccount account);
}
