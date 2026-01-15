package JavaPracticeProjects.PP01_BankAccountService.repository;

import JavaPracticeProjects.PP01_BankAccountService.entities.BankAccount;

public interface IBankAccountRepository {
    BankAccount getAccountById(String accountId);
    void save(BankAccount account);
}
