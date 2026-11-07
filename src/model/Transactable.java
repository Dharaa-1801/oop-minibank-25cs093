package model;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;

// Defines the deposit and withdrawal operations.
public interface Transactable {
    void deposit(long amount) throws InvalidAmountException;

    boolean withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException;
}
