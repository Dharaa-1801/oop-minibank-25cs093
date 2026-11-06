package com.minibank;

import com.minibank.exception.BankException;
import com.minibank.model.Account;
import com.minibank.repository.Repository;
import com.minibank.service.BankService;
import com.minibank.util.ReportGenerator;
import com.minibank.util.StatePersister;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public final class MiniBank {
    private static final Path DATA_FILE = Path.of("data", "accounts.dat");
    private static final Path LOG_FILE = Path.of("data", "transactions.log");

    private MiniBank() {
    }

    public record BankInfo(String name, String branch) {
    }

    public enum MenuOption {
        OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, STATEMENT, LIST, REPORT, EXIT
    }

    public static void main(String[] args) throws Exception {
        Repository<Account> repository = new Repository<>();
        if (Files.exists(DATA_FILE)) {
            for (Account account : StatePersister.load(DATA_FILE)) {
                repository.save(account);
            }
        }
        BankService service = new BankService(repository, LOG_FILE);
        Scanner scanner = new Scanner(System.in);
        System.out.println(new BankInfo("MiniBank", "Main Branch"));

        while (true) {
            printMenu();
            int choice = Integer.parseInt(scanner.nextLine());
            try {
                if (choice == 1) {
                    System.out.print("Owner name: ");
                    String owner = scanner.nextLine();
                    System.out.print("Opening balance: ");
                    long balance = Long.parseLong(scanner.nextLine());
                    Account account = service.openSavings(owner, balance);
                    System.out.println("Opened account: " + account.getAccountNumber());
                } else if (choice == 2) {
                    System.out.print("Account number and amount: ");
                    String[] parts = scanner.nextLine().split("\\s+");
                    service.deposit(parts[0], Long.parseLong(parts[1]));
                    System.out.println("Deposit completed.");
                } else if (choice == 3) {
                    System.out.print("Account number and amount: ");
                    String[] parts = scanner.nextLine().split("\\s+");
                    service.withdraw(parts[0], Long.parseLong(parts[1]));
                    System.out.println("Withdrawal completed.");
                } else if (choice == 4) {
                    System.out.print("From, to and amount: ");
                    String[] parts = scanner.nextLine().split("\\s+");
                    service.transfer(parts[0], parts[1], Long.parseLong(parts[2]));
                    System.out.println("Transfer completed.");
                } else if (choice == 5) {
                    System.out.print("Account number: ");
                    System.out.println(service.statement(scanner.nextLine()));
                } else if (choice == 6) {
                    service.listAccounts().forEach(System.out::println);
                } else if (choice == 7) {
                    Path report = ReportGenerator.generate(
                            LOG_FILE.getParent(), Path.of("data", "daily-report.txt"));
                    System.out.println(Files.readString(report));
                } else if (choice == 8) {
                    StatePersister.save(service.repository().findAll().toArray(Account[]::new), DATA_FILE);
                    System.out.println("Data saved. Goodbye.");
                    return;
                } else {
                    throw new IllegalArgumentException("Invalid menu choice");
                }
            } catch (BankException | IllegalArgumentException | java.io.IOException exception) {
                System.out.println("Operation failed: " + exception.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n1 Open account  2 Deposit  3 Withdraw  4 Transfer");
        System.out.println("5 Statement  6 List accounts  7 Report  8 Exit");
        System.out.print("Choose: ");
    }
}
