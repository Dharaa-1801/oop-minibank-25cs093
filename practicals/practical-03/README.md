# Practical 3 — Object behavior and nested classes

Make model objects readable, comparable, and cloneable.

## Requirements

- Override `Account.toString()`.
- Implement `equals` and `hashCode` using the account number.
- Add the static nested `Customer.Address` class with getters.
- Add an address to `Customer`.
- Implement `Cloneable` and a `Customer.clone()` method.
- Demonstrate `toString`, `equals`, and `instanceof` in `main`.

## Checkpoint

Two accounts with the same account number compare equally and print useful
details.

## Code

The implementation for this practical is in [`src/`](src/).
