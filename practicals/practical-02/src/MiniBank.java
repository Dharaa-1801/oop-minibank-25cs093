import java.util.Scanner;

public class MiniBank {
    public static void main(String[] args) {
        BankInfo bank = new BankInfo("MiniBank", "Main Branch");
        System.out.println(bank);

        try (Scanner scanner = new Scanner(System.in)) {
            int choice;
            do {
                System.out.println("\n1. Open Account");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Transfer");
                System.out.println("5. Exit");
                System.out.print("Choose: ");
                choice = Integer.parseInt(scanner.nextLine());

                MenuOption option = switch (choice) {
                    case 1 -> MenuOption.OPEN_ACCOUNT;
                    case 2 -> MenuOption.DEPOSIT;
                    case 3 -> MenuOption.WITHDRAW;
                    case 4 -> MenuOption.TRANSFER;
                    case 5 -> MenuOption.EXIT;
                    default -> throw new IllegalArgumentException("Invalid menu choice");
                };
                System.out.println(option == MenuOption.EXIT
                        ? "Goodbye."
                        : option + " — to be implemented in a later practical");
            } while (choice != 5);
        }
    }
}
