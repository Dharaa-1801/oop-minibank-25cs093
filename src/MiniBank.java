import java.util.Scanner;

public class MiniBank {
    public static void main(String[] args) {
        BankInfo bankInfo = new BankInfo("MiniBank", "Main Branch");
        System.out.println(bankInfo);

        Account[] accounts = {
                new Account("Alice", 1_000),
                new Account("Bob"),
                new Account("Charlie", 2_000)
        };

        accounts[0].deposit(500);
        accounts[1].deposit(750);
        accounts[2].withdraw(250);

        for (Account account : accounts) {
            System.out.println(account);
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
