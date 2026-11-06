package service;

import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.SavingsAccount;
import model.WithdrawRule;
import util.AnnotationValidator;
import util.Command;
import util.CommandParser;
import util.ReportGenerator;
import util.StatePersister;
import util.StatementFormatter;
import util.TransactionLog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static util.Validator.*;

public class MiniBank {
    public static void main(String[] args) {
        BankInfo bankInfo = new BankInfo("MiniBank", "Main Branch");
        System.out.println(bankInfo);

        Account[] accounts = {
                new SavingsAccount("Alice", 1_000, 500),
                new CurrentAccount("Bob", 500, 1_000),
                new FixedDepositAccount("Charlie", 2_000)
        };

        demonstrateExceptionHandling(accounts);
        demonstrateConcurrency();
        demonstrateTransactionEngine();
        demonstrateCollections(accounts);

        for (Account account : accounts) {
            System.out.println(account + ", interest rate=" + account.interestRate()
                    + "%, yearly interest=" + account.yearlyInterest());
            if (account instanceof SavingsAccount savingsAccount) {
                System.out.println("Savings minimum balance rule applies to "
                        + savingsAccount.getOwnerName());
            }
        }

        Account firstAccount = accounts[0];
        Account secondAccount = accounts[1];
        System.out.println("First account equals second account: "
                + firstAccount.equals(secondAccount));
        System.out.println("First account is an Account: "
                + (firstAccount instanceof Account));

        Customer customer = new Customer("Alice", "alice@example.com", "9876543210");
        customer.setAddress(new Customer.Address("12 Main Road", "Nadiad", "387001"));
        Customer clonedCustomer = customer.clone();
        System.out.println("Customer ID: " + customer.getCustomerId());
        System.out.println("Customer address city: " + customer.getAddress().getCity());
        System.out.println("Cloned customer ID: " + clonedCustomer.getCustomerId());

        demonstrateWithdrawRules(accounts[0]);
        demonstrateValidationAndCommands(accounts[0]);
        demonstrateAnnotationValidation();
        demonstrateTryWithResources();
        demonstratePersistence(accounts);

        try (Scanner scanner = new Scanner(System.in)) {
            MenuOption selectedOption;

            do {
                displayMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                selectedOption = switch (choice) {
                    case 1 -> MenuOption.OPEN_ACCOUNT;
                    case 2 -> MenuOption.DEPOSIT;
                    case 3 -> MenuOption.WITHDRAW;
                    case 4 -> MenuOption.TRANSFER;
                    case 5 -> MenuOption.EXIT;
                    default -> throw new IllegalArgumentException("Invalid menu choice");
                };

                if (selectedOption == MenuOption.EXIT) {
                    System.out.println("Goodbye!");
                } else {
                    System.out.println(selectedOption
                            + " - to be implemented in a later practical");
                }

            } while (selectedOption != MenuOption.EXIT);
        }
    }

    private static void demonstrateExceptionHandling(Account[] accounts) {
        try {
            accounts[0].deposit(500);
            accounts[1].deposit(750);
            System.out.println("Savings withdrawal allowed: "
                    + accounts[0].withdraw(1_000));
            System.out.println("Current withdrawal allowed: "
                    + accounts[1].withdraw(2_500));
            System.out.println("Fixed deposit withdrawal allowed: "
                    + accounts[2].withdraw(250));
            accounts[0].transfer(accounts[1], 100);
        } catch (InsufficientFundsException exception) {
            System.out.println("Withdrawal failed: " + exception.getMessage());
        } catch (InvalidAmountException exception) {
            System.out.println("Amount error: " + exception.getMessage());
        } catch (BankException exception) {
            System.out.println("Bank operation failed: " + exception.getMessage());
        } finally {
            System.out.println("Bank operation handling completed.");
        }

        try {
            accounts[0].deposit(0);
        } catch (InvalidAmountException exception) {
            System.out.println("Invalid amount handled: " + exception.getMessage());
        } finally {
            System.out.println("Invalid amount handling completed.");
        }

        try {
            accounts[0].transfer(accounts[1], 5_000);
        } catch (BankException exception) {
            System.out.println("Transfer failed: " + exception.getMessage());
        } finally {
            System.out.println("Transfer handling completed.");
        }
    }

    private static void demonstrateTryWithResources() {
        try (BankSession session = new BankSession()) {
            System.out.println("Processing a resource-safe bank session.");
        }
    }

    private static void demonstratePersistence(Account[] accounts) {
        Path stateFile = Path.of("data", "accounts.dat");
        Path logFile = Path.of("data", "logs", "transactions.log");
        Path reportFile = Path.of("data", "daily-report.txt");

        System.out.println("\n----- Persistence and Reporting -----");
        try {
            StatePersister.save(accounts, stateFile);
            Account[] loadedAccounts = StatePersister.load(stateFile);
            System.out.println("Reloaded accounts:");
            for (Account account : loadedAccounts) {
                System.out.println(account);
            }
            TransactionLog.append(logFile, "DEPOSIT 500 AC0001");
            TransactionLog.append(logFile, "WITHDRAW 200 AC0001");
            Path generatedReport = ReportGenerator.generate(logFile.getParent(), reportFile);
            System.out.println(Files.readString(generatedReport));
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println("Persistence operation failed: " + exception.getMessage());
        }
    }

    private static void demonstrateConcurrency() {
        Account account = new CurrentAccount("Concurrent User", 0, 0);
        int threadCount = 10;
        int depositsPerThread = 1_000;
        long amountPerDeposit = 1;
        Thread[] workers = new Thread[threadCount];

        System.out.println("\n----- Thread Safety Demo -----");
        for (int i = 0; i < threadCount; i++) {
            workers[i] = new Thread(
                    new AccountWorker(account, depositsPerThread, amountPerDeposit),
                    "AccountWorker-" + (i + 1));
            workers[i].start();
        }

        try {
            for (Thread worker : workers) {
                worker.join();
            }
            long expectedBalance = threadCount * depositsPerThread * amountPerDeposit;
            System.out.println("Expected balance: " + expectedBalance);
            System.out.println("Final balance: " + account.getBalance());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            System.out.println("Concurrency demo interrupted.");
        }
    }

    private static void demonstrateTransactionEngine() {
        Account account = new CurrentAccount("Processor User", 0, 0);
        TransactionProcessor processor = new TransactionProcessor();
        int taskCount = 20;

        System.out.println("\n----- Transaction Processor Demo -----");
        for (int i = 0; i < taskCount; i++) {
            processor.submit(TransactionProcessor.depositTask(account, 10));
        }

        try {
            processor.stop();
            System.out.println("Managed tasks completed: " + taskCount);
            System.out.println("Processed balance: " + account.getBalance());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            System.out.println("Transaction processor interrupted.");
        }

        TransactionBuffer buffer = new TransactionBuffer(2);
        Thread producer = new Thread(() -> {
            try {
                buffer.put("DEPOSIT 100");
                buffer.put("WITHDRAW 50");
                buffer.put("TRANSFER 25");
                System.out.println("Producer completed.");
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    System.out.println("Consumer processed: " + buffer.take());
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");
        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
            System.out.println("Producer-consumer coordination completed.");
            Account first = new CurrentAccount("Transfer A", 100, 0);
            Account second = new CurrentAccount("Transfer B", 100, 0);
            TransactionProcessor.transferSafely(first, second, 25);
            TransactionProcessor.transferSafely(second, first, 25);
            System.out.println("Ordered transfers completed without deadlock.");
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            System.out.println("Producer-consumer demo interrupted.");
        } catch (BankException exception) {
            System.out.println("Ordered transfer failed: " + exception.getMessage());
        }
    }

    private static void demonstrateCollections(Account[] accounts) {
        AccountStore store = new AccountStore();
        for (Account account : accounts) {
            store.add(account);
        }
        store.addTransaction("DEPOSIT 500 " + accounts[0].getAccountNumber());
        store.addTransaction("WITHDRAW 200 " + accounts[1].getAccountNumber());

        TransactionProcessor processor = new TransactionProcessor();
        for (Account account : accounts) {
            processor.addAccount(account);
        }

        System.out.println("\n----- Collections Demo -----");
        System.out.println("Thread-safe account count: " + processor.accountCount());
        System.out.println("Accounts sorted by balance:");
        for (Account account : store.sortedByBalance()) {
            System.out.println(account);
        }
        System.out.println("Transaction history entries: "
                + store.getTransactionHistory().size());
        try {
            processor.stop();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            System.out.println("Collections demo interrupted.");
        }
    }

    private static void demonstrateValidationAndCommands(Account account) {
        System.out.println("\n----- Validation Tests -----");
        printValidation("Mobile", isValidMobile("9876543210"),
                isValidMobile("12345"));
        printValidation("Email", isValidEmail("alice@example.com"),
                isValidEmail("alice@example"));
        printValidation("PAN", isValidPan("ABCDE1234F"),
                isValidPan("ABC123"));
        printValidation("IFSC", isValidIfsc("SBIN0001234"),
                isValidIfsc("INVALID"));

        Command command = CommandParser.parse(
                "DEPOSIT " + account.getAccountNumber() + " 500");
        System.out.println("\n----- Parsed Command -----");
        System.out.println("Type: " + command.type());
        System.out.println("Account: " + command.accountNumber());
        System.out.println("Amount: " + command.amount());

        System.out.println("\n" + StatementFormatter.buildStatement(account));
    }

    private static void demonstrateAnnotationValidation() {
        Account invalidAccount = new SavingsAccount("Invalid Account", -100, 0);
        System.out.println("\n----- Annotation Validation -----");
        for (String error : AnnotationValidator.validate(invalidAccount)) {
            System.out.println(error);
        }
    }

    private static void demonstrateWithdrawRules(Account account) {
        WithdrawRule anonymousRule = new WithdrawRule() {
            @Override
            public boolean allow(Account candidate, long amount) {
                return candidate.canWithdraw(amount);
            }
        };
        WithdrawRule lambdaRule = (candidate, amount) -> candidate.canWithdraw(amount);

        System.out.println("\nAnonymous withdraw rule: "
                + anonymousRule.allow(account, 100));
        System.out.println("Lambda withdraw rule: " + lambdaRule.allow(account, 100));
    }

    private static void printValidation(String name, boolean valid, boolean invalid) {
        System.out.println(name + " valid input: " + valid);
        System.out.println(name + " invalid input: " + invalid);
    }

    private static void displayMenu() {
        System.out.println("\n----- MiniBank Menu -----");
        System.out.println("1. Open Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }
}
