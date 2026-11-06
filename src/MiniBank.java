import java.util.Scanner;

public class MiniBank {
    public static void main(String[] args) {
        BankInfo bankInfo = new BankInfo("MiniBank", "Main Branch");
        System.out.println(bankInfo);

        Account[] accounts = {
                new SavingsAccount("Alice", 1_000, 500),
                new CurrentAccount("Bob", 500, 1_000),
                new FixedDepositAccount("Charlie", 2_000)
        };

        accounts[0].deposit(500);
        accounts[1].deposit(750);
        System.out.println("Savings withdrawal allowed: " + accounts[0].withdraw(1_000));
        System.out.println("Current withdrawal allowed: " + accounts[1].withdraw(2_500));
        System.out.println("Fixed deposit withdrawal allowed: " + accounts[2].withdraw(250));

        for (Account account : accounts) {
            System.out.println(account + ", interest rate=" + account.interestRate() + "%");
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

        demonstrateValidationAndCommands(accounts[0]);

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

    private static void demonstrateValidationAndCommands(Account account) {
        System.out.println("\n----- Validation Tests -----");
        printValidation("Mobile", Validator.isValidMobile("9876543210"),
                Validator.isValidMobile("12345"));
        printValidation("Email", Validator.isValidEmail("alice@example.com"),
                Validator.isValidEmail("alice@example"));
        printValidation("PAN", Validator.isValidPan("ABCDE1234F"),
                Validator.isValidPan("ABC123"));
        printValidation("IFSC", Validator.isValidIfsc("SBIN0001234"),
                Validator.isValidIfsc("INVALID"));

        Command command = CommandParser.parse(
                "DEPOSIT " + account.getAccountNumber() + " 500");
        System.out.println("\n----- Parsed Command -----");
        System.out.println("Type: " + command.type());
        System.out.println("Account: " + command.accountNumber());
        System.out.println("Amount: " + command.amount());

        System.out.println("\n" + StatementFormatter.buildStatement(account));
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
