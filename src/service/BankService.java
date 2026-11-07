package service;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import util.Command;
import util.ReportGenerator;
import util.Repository;
import util.StatementFormatter;
import util.TransactionLog;
import util.Validator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Provides the main banking operations used by the console application.
public final class BankService {
    private final Repository<Account> repository = new Repository<>();
    private final Path logFile;

    public BankService(Account[] accounts, Path logFile) {
        this.logFile = logFile;
        if (accounts != null) {
            repository.saveAll(accounts);
        }
    }

    public Account openAccount(String ownerName, String type, long openingBalance)
            throws InvalidAmountException {
        if (ownerName == null || ownerName.isBlank()) {
            throw new IllegalArgumentException("Owner name must not be blank");
        }
        if (openingBalance < 0) {
            throw new InvalidAmountException(openingBalance);
        }

        Account account = switch (type.toUpperCase()) {
            case "SAVINGS" -> new SavingsAccount(ownerName, openingBalance, 500);
            case "CURRENT" -> new CurrentAccount(ownerName, openingBalance, 1_000);
            case "FIXED" -> new FixedDepositAccount(ownerName, openingBalance);
            default -> throw new IllegalArgumentException(
                    "Account type must be SAVINGS, CURRENT, or FIXED");
        };
        repository.save(account);
        return account;
    }

    public void deposit(String accountNumber, long amount)
            throws BankException, IOException {
        Account account = findAccount(accountNumber);
        account.deposit(amount);
        appendLog("DEPOSIT " + amount + " " + accountNumber);
    }

    public void withdraw(String accountNumber, long amount)
            throws BankException, IOException {
        Account account = findAccount(accountNumber);
        account.withdraw(amount);
        appendLog("WITHDRAW " + amount + " " + accountNumber);
    }

    public void transfer(String fromAccount, String toAccount, long amount)
            throws BankException, IOException {
        Account from = findAccount(fromAccount);
        Account to = findAccount(toAccount);
        TransactionProcessor.transferSafely(from, to, amount);
        appendLog("TRANSFER " + amount + " " + fromAccount + " " + toAccount);
    }

    public String statement(String accountNumber) throws AccountNotFoundException {
        return StatementFormatter.buildStatement(findAccount(accountNumber));
    }

    public ArrayList<Account> listAccounts() {
        return repository.findAll();
    }

    public void processBatch(List<Command> commands) throws BankException, IOException {
        TransactionProcessor processor = new TransactionProcessor();
        for (Account account : repository.findAll()) {
            processor.addAccount(account);
        }
        for (Command command : commands) {
            Account account = processor.getAccount(command.accountNumber());
            if (account == null) {
                throw new AccountNotFoundException(command.accountNumber());
            }
            processor.submit(() -> executeBatchCommand(account, command));
        }
        try {
            processor.stop();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new BankException("Batch processing interrupted", exception);
        }
    }

    public Path generateReport(Path logsDirectory, Path reportFile) throws IOException {
        return ReportGenerator.generate(logsDirectory, reportFile);
    }

    private Account findAccount(String accountNumber) throws AccountNotFoundException {
        if (!Validator.isValidAccountNumber(accountNumber)) {
            throw new AccountNotFoundException(accountNumber);
        }
        Account account = repository.findById(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }

    private void executeBatchCommand(Account account, Command command) {
        try {
            switch (command.type()) {
                case DEPOSIT -> account.deposit(command.amount());
                case WITHDRAW -> account.withdraw(command.amount());
                case TRANSFER -> throw new IllegalArgumentException(
                        "Batch transfer requires a destination account");
            }
            appendLog(command.type() + " " + command.amount()
                    + " " + command.accountNumber());
        } catch (BankException | IOException exception) {
            throw new IllegalStateException("Batch transaction failed", exception);
        }
    }

    private void appendLog(String line) throws IOException {
        TransactionLog.append(logFile, line);
    }
}
