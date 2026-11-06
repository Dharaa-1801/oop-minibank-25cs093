public class Account
{
    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean isActive;
    private static long accountCounter;

    private static String generateAccountNumber()
    {
        accountCounter++;
        return "ACC" + accountCounter;
    }
    public Account(String ownerName,long balance)
    {
        this.ownerName=ownerName;
        this.balance=balance;
        this.isActive=true;
        this.accountNumber = generateAccountNumber();
    }
    public Account(String ownerName)
    {
        this(ownerName, 0);
    }
    public boolean deposit(long amount)
    {
        if(amount<=0)
        {
            System.out.println("Deposit amount must be positive");
            return false;
        }
        balance+=amount;
        return true;
    }
    public boolean withdraw(long amount)
    {
        if(amount<=0)
        {
            System.out.println("Withdraw amount must be positive");
            return false;
        }
        if(amount>balance)
        {
            System.out.println("Insufficient balance");
            return false;
        }
        balance-=amount;
        return true;
    }

    public long getBalance()
    {
        return balance;
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }
    public String getOwnerName()
    {
        return ownerName;
    }
    public boolean isActive()
    {
        return isActive;
    }
    
    public String toString() {
    return "Account{" +
            "accountNumber='" + accountNumber + '\'' +
            ", ownerName='" + ownerName + '\'' +
            ", balance=" + balance +
            '}';
}

public boolean equals(Object o) {

    if (this == o)
        return true;

    if (!(o instanceof Account))
        return false;

    Account other = (Account) o;

    return accountNumber.equals(other.accountNumber);
}
}
