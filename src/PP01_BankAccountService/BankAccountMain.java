package PP01_BankAccountService;

import PP01_BankAccountService.dto.DepositRequestDTO;
import PP01_BankAccountService.dto.TransferRequestDTO;
import PP01_BankAccountService.dto.WithdrawRequestDTO;
import PP01_BankAccountService.entities.BankAccount;
import PP01_BankAccountService.repository.IBankAccountRepository;
import PP01_BankAccountService.repository.InMemoryBankAccountRepository;

public class BankAccountMain {
    public static void main(String[] args) {
        // Infrastructure
        IBankAccountRepository repository =
                new InMemoryBankAccountRepository();

        // Seed data
        repository.save(new BankAccount("A1", 1000));
        repository.save(new BankAccount("A2", 500));

        // Application service
        BankAccountService service =
                new BankAccountService(repository);

        // ---- Deposit ----
        service.deposit(new DepositRequestDTO("A1", 200));

        // ---- Withdraw ----
        service.withdraw(new WithdrawRequestDTO("A2", 100));

        // ---- Transfer ----
        service.transfer(
                new TransferRequestDTO("A1", "A2", 300)
        );

        // ---- Query ----
        System.out.println("A1 balance: " +
                service.getBalance("A1"));

        System.out.println("A2 balance: " +
                service.getBalance("A2"));
    }
}
