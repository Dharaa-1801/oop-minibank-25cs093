package exception;

// Indicates that a transaction amount is invalid.
public class InvalidAmountException extends BankException {
    public InvalidAmountException(long amount) {
        super("Amount must be greater than zero: " + amount);
    }
}
