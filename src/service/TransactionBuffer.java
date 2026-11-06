package service;

public final class TransactionBuffer {
    private final String[] transactions;
    private int count;

    public TransactionBuffer(int capacity) {
        transactions = new String[capacity];
    }

    public synchronized void put(String transaction) throws InterruptedException {
        while (count == transactions.length) {
            wait();
        }
        transactions[count++] = transaction;
        notify();
    }

    public synchronized String take() throws InterruptedException {
        while (count == 0) {
            wait();
        }
        String transaction = transactions[--count];
        transactions[count] = null;
        notify();
        return transaction;
    }
}
