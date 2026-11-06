import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class AccountStore {
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    public void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account find(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public boolean remove(String accountNumber) {
        return accounts.remove(accountNumber) != null;
    }

    public List<Account> sortedByBalance() {
        List<Account> result = new ArrayList<>(accounts.values());
        result.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return result;
    }
}
