public class Account {
    private final String accountNumber;
    private String ownerName;
    private long balance;            // whole rupees
    private boolean active;
    private static int counter = 1;

    public Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }
    public Account(String ownerName) { this(ownerName, 0); }   // constructor chaining
    private static String generateAccountNumber() { return String.format("AC%04d", counter++); }

    public void deposit(long amount) { if (amount > 0) balance += amount; }
    public boolean withdraw(long amount) {
        if (amount > 0 && amount <= balance) { balance -= amount; return true; }
        return false;                                          // insufficient funds
    }
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName()     { return ownerName; }
    public long getBalance()         { return balance; }
    public boolean isActive()        { return active; }
    // ---- new in Practical 3 ----
    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account other = (Account) o;
        return accountNumber.equals(other.accountNumber);
    }

    public int hashCode() {
        return java.util.Objects.hashCode(accountNumber);
    }
}