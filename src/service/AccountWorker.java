package service;

import exception.InvalidAmountException;
import model.Account;

public final class AccountWorker implements Runnable {
    private final Account account;
    private final int times;
    private final long amount;

    public AccountWorker(Account account, int times, long amount) {
        this.account = account;
        this.times = times;
        this.amount = amount;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " started.");
        try {
            for (int i = 0; i < times; i++) {
                account.deposit(amount);
            }
        } catch (InvalidAmountException exception) {
            System.out.println(threadName + " failed: " + exception.getMessage());
        } finally {
            System.out.println(threadName + " finished.");
        }
    }
}
