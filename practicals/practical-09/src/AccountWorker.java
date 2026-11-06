import java.util.concurrent.CountDownLatch;

public final class AccountWorker implements Runnable {
    private final Account account;
    private final int times;
    private final long amount;
    private final CountDownLatch start;

    public AccountWorker(Account account, int times, long amount, CountDownLatch start) {
        this.account = account;
        this.times = times;
        this.amount = amount;
        this.start = start;
    }

    @Override
    public void run() {
        try {
            start.await();
            for (int i = 0; i < times; i++) {
                account.deposit(amount);
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}
