package exception;

public class InvalidAmountException extends BankException {
    public InvalidAmountException(long amount) {
        super("Amount must be greater than zero: " + amount);
    }
}
