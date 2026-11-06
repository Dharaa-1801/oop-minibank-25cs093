# Practical 5 — Abstract accounts and polymorphism

Support different account rules through one common `Account` type.

## Requirements

- Make `Account` abstract.
- Add abstract `interestRate()` and `canWithdraw(long)` methods.
- Implement `SavingsAccount`, `CurrentAccount`, and
  `FixedDepositAccount`.
- Apply minimum-balance, overdraft, and locked-deposit rules.
- Initialise inherited fields with `super(...)`.
- Store all three types in `Account[]` and call methods polymorphically.

## Checkpoint

Each account type returns its own interest rate and withdrawal decision.

## Code

The account hierarchy implementation is in [`src/`](src/).
