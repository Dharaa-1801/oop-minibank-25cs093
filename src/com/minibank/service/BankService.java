package com.minibank.service;

import com.minibank.exception.AccountNotFoundException;
import com.minibank.exception.BankException;
import com.minibank.exception.InsufficientFundsException;
import com.minibank.exception.InvalidAmountException;
import com.minibank.model.Account;
import com.minibank.model.CurrentAccount;
import com.minibank.model.FixedDepositAccount;
import com.minibank.model.SavingsAccount;
import com.minibank.repository.Repository;
import com.minibank.util.StatementFormatter;
import com.minibank.util.TransactionLog;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;

public final class BankService {
    private final Repository<Account> accounts;
    private final Path logFile;

    public BankService(Repository<Account> accounts, Path logFile) {
        this.accounts = accounts;
        this.logFile = logFile;
    }

    public Account openSavings(String owner, long openingBalance) {
        Account account = new SavingsAccount(owner, openingBalance, 500);
        accounts.save(account);
        return account;
    }

    public Account openCurrent(String owner, long openingBalance) {
        Account account = new CurrentAccount(owner, openingBalance, 1_000);
        accounts.save(account);
        return account;
    }

    public Account openFixedDeposit(String owner, long openingBalance) {
        Account account = new FixedDepositAccount(owner, openingBalance);
        accounts.save(account);
        return account;
    }

    public void deposit(String accountNumber, long amount)
            throws BankException, IOException {
        Account account = require(accountNumber);
        account.deposit(amount);
        TransactionLog.append(logFile, "DEPOSIT " + amount + " " + accountNumber);
    }

    public void withdraw(String accountNumber, long amount)
            throws BankException, IOException {
        Account account = require(accountNumber);
        account.withdraw(amount);
        TransactionLog.append(logFile, "WITHDRAW " + amount + " " + accountNumber);
    }

    public void transfer(String fromNumber, String toNumber, long amount)
            throws BankException, IOException {
        Account from = require(fromNumber);
        Account to = require(toNumber);
        Account first = from.compareTo(to) < 0 ? from : to;
        Account second = first == from ? to : from;
        synchronized (first) {
            synchronized (second) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
        TransactionLog.append(logFile, "WITHDRAW " + amount + " " + fromNumber);
        TransactionLog.append(logFile, "DEPOSIT " + amount + " " + toNumber);
    }

    public String statement(String accountNumber) throws AccountNotFoundException {
        return StatementFormatter.buildStatement(require(accountNumber));
    }

    public ArrayList<Account> listAccounts() {
        ArrayList<Account> result = accounts.findAll();
        result.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return result;
    }

    public Account require(String accountNumber) throws AccountNotFoundException {
        Account account = accounts.findById(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }

    public Repository<Account> repository() {
        return accounts;
    }
}
