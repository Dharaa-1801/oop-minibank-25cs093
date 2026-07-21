import java.util.Scanner;

public class MiniBank {
    public static void main(String[] args) {
        BankInfo info = new BankInfo("MiniBank", "Nadiad");
        System.out.println("================================================");
        System.out.println("   " + info.name() + " - " + info.branch() + " Branch");
        System.out.println("================================================");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readChoice(scanner);
            MenuOption option = toOption(choice);

            if (option == null) {
                System.out.println("Invalid choice. Please enter a number from 1 to 5.\n");
                continue;
            }

            String message = switch (option) {
                case OPEN_ACCOUNT -> "Open Account - to be implemented in a later lab.";
                case DEPOSIT      -> "Deposit - to be implemented in a later lab.";
                case WITHDRAW     -> "Withdraw - to be implemented in a later lab.";
                case TRANSFER     -> "Transfer - to be implemented in a later lab.";
                case EXIT         -> "Thank you for using MiniBank. Goodbye!";
            };
            System.out.println(message + "\n");

            if (option == MenuOption.EXIT) running = false;
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("Please choose an option:");
        System.out.println("  1. Open Account");
        System.out.println("  2. Deposit");
        System.out.println("  3. Withdraw");
        System.out.println("  4. Transfer");
        System.out.println("  5. Exit");
        System.out.print("Enter your choice (1-5): ");
    }

    private static int readChoice(Scanner scanner) {
        if (scanner.hasNextInt()) return scanner.nextInt();
        if (scanner.hasNext()) scanner.next();
        return -1;
    }

    private static MenuOption toOption(int choice) {
        return switch (choice) {
            case 1 -> MenuOption.OPEN_ACCOUNT;
            case 2 -> MenuOption.DEPOSIT;
            case 3 -> MenuOption.WITHDRAW;
            case 4 -> MenuOption.TRANSFER;
            case 5 -> MenuOption.EXIT;
            default -> null;
        };
    }
}
