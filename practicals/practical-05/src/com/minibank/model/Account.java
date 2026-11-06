package com.minibank.model;

import com.minibank.exception.InsufficientFundsException;
import com.minibank.exception.InvalidAmountException;
import com.minibank.model.annotation.Id;
import com.minibank.model.annotation.Positive;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;

public abstract class Account implements Transactable, InterestBearing,
        Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;
    private static long accountCounter;

    @Id
    private final String accountNumber;
    private final String ownerName;
    @Positive
    private long balance;
    private boolean active = true;

    protected Account(String ownerName, long openingBalance) {
        if (ownerName == null || ownerName.isBlank()) {
            throw new IllegalArgumentException("Owner name cannot be blank");
        }
        if (openingBalance < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative");
        }
        this.accountNumber = String.format("AC%04d", ++accountCounter);
        this.ownerName = ownerName.trim();
        this.balance = openingBalance;
    }

    public abstract boolean canWithdraw(long amount);

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
        balance += amount;
    }

    @Override
    public synchronized boolean withdraw(long amount)
            throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
        if (!canWithdraw(amount)) {
            long availableShortfall = amount - balance;
            throw new InsufficientFundsException(Math.max(0, availableShortfall));
        }
        balance -= amount;
        return true;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public synchronized long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public int compareTo(Account other) {
        return accountNumber.compareTo(other.accountNumber);
    }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | balance=" + balance;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Account account
                && accountNumber.equals(account.accountNumber);
    }

    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }

    private void readObject(ObjectInputStream input)
            throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        long number = Long.parseLong(accountNumber.substring(2));
        synchronized (Account.class) {
            accountCounter = Math.max(accountCounter, number);
        }
    }
}
