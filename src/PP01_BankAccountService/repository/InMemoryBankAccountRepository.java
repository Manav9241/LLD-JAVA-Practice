package PP01_BankAccountService.repository;

import PP01_BankAccountService.entities.BankAccount;

import java.util.HashMap;
import java.util.Map;

public class InMemoryBankAccountRepository implements IBankAccountRepository{
    private final Map<String, BankAccount> dbStore = new HashMap<>();

    @Override
    public BankAccount getAccountById(String accountId) {
        BankAccount account = dbStore.get(accountId);
        if(account == null) {
            throw new IllegalArgumentException("Account not found: " + accountId);
        }
        return account;
    }

    @Override
    public void save(BankAccount account) {
        String accountId = account.getAccountId();
        dbStore.put(accountId, account);
    }
}
