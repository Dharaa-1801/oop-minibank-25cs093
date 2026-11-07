package service;

import exception.InvalidAmountException;
import model.Account;
import java.util.Objects;

// Performs a fixed number of deposits for one account on a worker thread.
public final class AccountWorker implements Runnable {
    private final Account account;
    private final int depositCount;
    private final long amount;

    public AccountWorker(Account account, int depositCount, long amount) {
        this.account = Objects.requireNonNull(account, "account must not be null");

        // Validate the worker configuration before the thread starts.
        if (depositCount < 0) {
            throw new IllegalArgumentException("depositCount must not be negative");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        this.depositCount = depositCount;
        this.amount = amount;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " started.");
        try {
            for (int depositNumber = 0; depositNumber < depositCount; depositNumber++) {
                account.deposit(amount);
            }
        } catch (InvalidAmountException exception) {
            System.out.println(threadName + " failed: " + exception.getMessage());
        } finally {
            System.out.println(threadName + " finished.");
        }
    }
}
