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
import util.StatementFormatter;

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
