package service;

import exception.BankException;
import exception.InvalidAmountException;
import model.Account;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;

public final class TransactionProcessor {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    public void submit(Runnable task) {
        executor.execute(task);
    }

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public int accountCount() {
        return accounts.size();
    }

    public void stop() throws InterruptedException {
        executor.shutdown();
        if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
            executor.shutdownNow();
            throw new IllegalStateException("Transaction processor did not stop");
        }
    }

    public static void transferSafely(Account from, Account to, long amount)
            throws BankException {
        Account first = from.getAccountNumber().compareTo(to.getAccountNumber()) < 0
                ? from : to;
        Account second = first == from ? to : from;

        synchronized (first) {
            synchronized (second) {
                from.transfer(to, amount);
            }
        }
    }

    public static Runnable depositTask(Account account, long amount) {
        return () -> {
            try {
                account.deposit(amount);
            } catch (InvalidAmountException exception) {
                throw new IllegalStateException("Deposit task failed", exception);
            }
        };
    }
}
