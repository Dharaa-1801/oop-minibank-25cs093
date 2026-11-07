package util;

// Stores the details of one parsed transaction command.
public record Command(TransactionType type, String accountNumber, long amount) {
}
