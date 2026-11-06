# Practical 2 — Customer and Account classes

Build the encapsulated data model used by the bank.

## Requirements

- Create `Customer` with private name, email, mobile, and final customer ID.
- Generate customer IDs with a private static counter.
- Create `Account` with account number, owner, balance, and active state.
- Generate account numbers with a private static counter.
- Add both `Account` constructors, including constructor chaining with `this`.
- Change balance only through `deposit` and `withdraw`.
- Create an array of accounts and demonstrate deposits and withdrawals.

## Checkpoint

Account balances are printed after valid and insufficient withdrawals.

## Code

The model implementation for this practical is in [`src/`](src/).
