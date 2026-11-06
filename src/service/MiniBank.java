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
import util.Repository;
import util.StatePersister;
import util.StatementFormatter;
import util.TransactionLog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.ArrayList;

import static util.Validator.*;

public class MiniBank {
    public static void main(String[] args) {
        Path stateFile = Path.of("data", "accounts.dat");
        Path logDirectory = Path.of("data", "logs");
        Path reportFile = Path.of("data", "daily-report.txt");
        Account[] accounts = loadAccounts(stateFile);
        BankService service = new BankService(accounts, logDirectory.resolve("transactions.log"));

        System.out.println(new BankInfo("MiniBank", "Main Branch"));
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                displayMenu();
                try {
                    switch (Integer.parseInt(scanner.nextLine())) {
                        case 1 -> openAccount(scanner, service);
                        case 2 -> deposit(scanner, service);
                        case 3 -> withdraw(scanner, service);
                        case 4 -> transfer(scanner, service);
                        case 5 -> System.out.println(service.statement(
                                prompt(scanner, "Account number: ")));
                        case 6 -> service.listAccounts().forEach(System.out::println);
                        case 7 -> System.out.println(Files.readString(
                                service.generateReport(logDirectory, reportFile)));
                        case 8 -> {
                            running = false;
                            System.out.println("Goodbye!");
                        }
                        default -> System.out.println("Invalid menu choice.");
                    }
                } catch (BankException | IOException | NumberFormatException exception) {
                    System.out.println("Operation failed: " + exception.getMessage());
                }
            }
        }
        try {
            StatePersister.save(service.listAccounts().toArray(Account[]::new), stateFile);
            System.out.println("Account state saved.");
        } catch (IOException exception) {
            System.out.println("Could not save account state: " + exception.getMessage());
        }
    }

    private static Account[] loadAccounts(Path stateFile) {
        try {
            if (Files.exists(stateFile)) {
                return StatePersister.load(stateFile);
            }
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println("Could not load saved accounts: " + exception.getMessage());
        }
        return new Account[0];
    }

    private static void openAccount(Scanner scanner, BankService service)
            throws InvalidAmountException {
        Account account = service.openAccount(prompt(scanner, "Owner name: "),
                prompt(scanner, "Type (SAVINGS/CURRENT/FIXED): "),
                Long.parseLong(prompt(scanner, "Opening balance: ")));
        System.out.println("Opened account: " + account);
    }

    private static void deposit(Scanner scanner, BankService service)
            throws BankException, IOException {
        service.deposit(prompt(scanner, "Account number: "),
                Long.parseLong(prompt(scanner, "Amount: ")));
        System.out.println("Deposit completed.");
    }

    private static void withdraw(Scanner scanner, BankService service)
            throws BankException, IOException {
        service.withdraw(prompt(scanner, "Account number: "),
                Long.parseLong(prompt(scanner, "Amount: ")));
        System.out.println("Withdrawal completed.");
    }

    private static void transfer(Scanner scanner, BankService service)
            throws BankException, IOException {
        service.transfer(prompt(scanner, "From account: "),
                prompt(scanner, "To account: "),
                Long.parseLong(prompt(scanner, "Amount: ")));
        System.out.println("Transfer completed.");
    }

    private static String prompt(Scanner scanner, String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
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

    private static void demonstrateRepository(Account[] accounts) {
        Repository<Account> repository = new Repository<>();
        repository.saveAll(accounts);

        System.out.println("\n----- Generic Repository Demo -----");
        System.out.println("Fetched by id: "
                + repository.findById(accounts[0].getAccountNumber()));
        System.out.println("All repository accounts:");
        for (Account account : repository.findAll()) {
            System.out.println(account);
        }
        System.out.println("Average sample: "
                + Repository.average(new Integer[]{100, 200, 300}));
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
        System.out.println("5. Statement");
        System.out.println("6. List accounts");
        System.out.println("7. Daily report");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }
}
