package model;

@FunctionalInterface
// Defines how an account approves a withdrawal.
public interface WithdrawRule {
    boolean allow(Account account, long amount);
}
