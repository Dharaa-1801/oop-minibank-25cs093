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
            System.out.printf("%s - %s: Rs. %d%n",
                    account.getAccountNumber(),
                    account.getOwnerName(),
                    account.getBalance());
        }

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
