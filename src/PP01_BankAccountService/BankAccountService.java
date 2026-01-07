package PP01_BankAccountService;

import PP01_BankAccountService.dto.DepositRequestDTO;
import PP01_BankAccountService.dto.TransferRequestDTO;
import PP01_BankAccountService.dto.WithdrawRequestDTO;
import PP01_BankAccountService.entities.BankAccount;
import PP01_BankAccountService.repository.IBankAccountRepository;

public class BankAccountService {
    private final IBankAccountRepository repository;

    public BankAccountService(IBankAccountRepository repository) {
        this.repository = repository;
    }

    public void deposit(DepositRequestDTO request) {
        BankAccount account = repository.getAccountById(request.getAccountId());

        account.deposit(request.getAmount());

        repository.save(account);
    }

    public void withdraw(WithdrawRequestDTO request) {
        BankAccount account = repository.getAccountById(request.getAccountId());

        account.withdraw(request.getAmount());

        repository.save(account);
    }

    public void transfer(TransferRequestDTO request) {
        BankAccount fromAccount = repository.getAccountById(request.getFromAccountId());
        BankAccount toAccount = repository.getAccountById(request.getToAccountId());
        double amount = request.getAmount();

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        repository.save(fromAccount);
        repository.save(toAccount);
    }

    public double getBalance(String accountId) {
        BankAccount account = repository.getAccountById(accountId);
        return account.getBalance();
    }
}
