package service;

// Represents a closeable banking session.
public final class BankSession implements AutoCloseable {
    public BankSession() {
        System.out.println("Bank session opened.");
    }

    @Override
    public void close() {
        System.out.println("Bank session closed.");
    }
}
