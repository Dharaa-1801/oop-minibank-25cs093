package util;

import model.Account;

public final class StatementFormatter {
    private StatementFormatter() {
    }

    public static String buildStatement(Account account) {
        StringBuilder statement = new StringBuilder();
        statement.append("----- Account Statement -----\n");
        statement.append("Account number: ").append(account.getAccountNumber()).append('\n');
        statement.append("Owner name: ").append(account.getOwnerName()).append('\n');
        statement.append("Balance: Rs. ").append(account.getBalance()).append('\n');
        statement.append("Active: ").append(account.isActive());
        return statement.toString();
    }
}
