import java.util.*;
public class MiniBank
{
    record BankInfo(String bankName,String branch)
    {
            
    }
    enum MenuOption {OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER ,EXIT};

    public static void main(String []args)
    {
        Scanner sc=new Scanner(System.in);
        BankInfo bankinfo=new BankInfo("BOB","NAVSARI");
        System.out.println(bankinfo);

        Account[] accounts = new Account[3];

accounts[0] = new Account("Alice", 1000);
accounts[1] = new Account("Bob", 500);
accounts[2] = new Account("Charlie", 2000);

accounts[0].deposit(500);
accounts[1].withdraw(200);
accounts[2].withdraw(2500);

for (Account account : accounts) {
    System.out.println(
        account.getAccountNumber() + " : " +
        account.getOwnerName() + " : ₹" +
        account.getBalance()
    );
}


        System.out.println("Mobile:");
        System.out.println(Validator.isValidMobile("9876543210"));
        System.out.println(Validator.isValidMobile("12345"));

        System.out.println("\nEmail:");
        System.out.println(Validator.isValidEmail("abc@gmail.com"));
        System.out.println(Validator.isValidEmail("abc@gmail"));

        System.out.println("\nPAN:");
        System.out.println(Validator.isValidPan("ABCDE1234F"));
        System.out.println(Validator.isValidPan("ABC123"));

        System.out.println("\nIFSC:");
        System.out.println(Validator.isValidIfsc("SBIN0001234"));
        System.out.println(Validator.isValidIfsc("INVALID"));

        Command command =
                CommandParser.parse("DEPOSIT AC0001 500");

        System.out.println("\nCommand:");
        System.out.println("Type: " + command.type());
        System.out.println("Account: " + command.accountNumber());
        System.out.println("Amount: " + command.amount());

        Account account = new Account("Alice", 1000);

        System.out.println("\nStatement:");
        System.out.println(
                StatementFormatter.buildStatement(account)
        );
    

        int choice=0;
        do
        {
            System.out.println("\n------ MENU ------");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

        choice = sc.nextInt();
        switch(choice)
        {
            case 1:
                System.out.println("Open Account");
                break;

            case 2:
                System.out.println("Deposit");
                break;

                case 3:
                    System.out.println("Withdraw");
                    break;

            case 4:
                System.out.println("Transfer");
                break;

            case 5:
                System.out.println("Exit");
                break;

        }
    }while(choice!=5);
    sc.close();


    }
}


