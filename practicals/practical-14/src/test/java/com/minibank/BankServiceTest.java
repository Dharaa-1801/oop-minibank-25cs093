package com.minibank;

import com.minibank.exception.InsufficientFundsException;
import com.minibank.model.Account;
import com.minibank.repository.Repository;
import com.minibank.service.BankService;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankServiceTest {
    @Test
    void depositIncreasesBalance() throws Exception {
        BankService service = new BankService(new Repository<>(), temporaryLog());
        Account account = service.openCurrent("Asha", 100);

        service.deposit(account.getAccountNumber(), 50);

        assertEquals(150, account.getBalance());
    }

    @Test
    void withdrawalOverLimitThrows() {
        BankService service = new BankService(new Repository<>(), Path.of("target", "test.log"));
        Account account = service.openSavings("Ravi", 600);

        assertThrows(InsufficientFundsException.class,
                () -> service.withdraw(account.getAccountNumber(), 200));
    }

    @Test
    void concurrentDepositsRemainCorrect() throws Exception {
        BankService service = new BankService(new Repository<>(), temporaryLog());
        Account account = service.openCurrent("Mina", 0);
        Thread[] workers = new Thread[10];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < 100; j++) {
                        service.deposit(account.getAccountNumber(), 1);
                    }
                } catch (Exception exception) {
                    throw new IllegalStateException(exception);
                }
            });
            workers[i].start();
        }
        for (Thread worker : workers) {
            worker.join();
        }
        assertEquals(1_000, account.getBalance());
    }

    private static Path temporaryLog() throws Exception {
        return Files.createTempFile("minibank-test-", ".log");
    }
}
