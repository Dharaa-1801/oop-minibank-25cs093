package model;

// Defines the interest-rate behavior of an account.
public interface InterestBearing {
    double interestRate();

    long getBalance();

    default double yearlyInterest() {
        return interestRate() * getBalance() / 100;
    }
}
