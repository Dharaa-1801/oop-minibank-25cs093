package service;

import exception.InsufficientFundsException;
import model.CurrentAccount;
import model.Account;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankServiceTest {
    @Test
    void depositIncreasesBalance() throws Exception {
        Path log = Files.createTempFile("minibank-test", ".log");
        Account account = new CurrentAccount("Deposit User", 0, 0);
        BankService service = new BankService(new Account[]{account}, log);

        service.deposit(account.getAccountNumber(), 500);

        assertEquals(500, account.getBalance());
        Files.deleteIfExists(log);
    }

    @Test
    void withdrawalOverLimitThrowsException() {
        Account account = new CurrentAccount("Withdrawal User", 100, 0);

        assertThrows(InsufficientFundsException.class, () -> account.withdraw(101));
    }

    @Test
    void concurrentDepositsKeepBalanceCorrect() throws InterruptedException {
        Account account = new CurrentAccount("Concurrent User", 0, 0);
        int threadCount = 10;
        int depositsPerThread = 100;
        Thread[] workers = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            workers[i] = new Thread(new AccountWorker(account, depositsPerThread, 1));
            workers[i].start();
        }
        for (Thread worker : workers) {
            worker.join();
        }

        assertEquals(threadCount * depositsPerThread, account.getBalance());
    }
}
