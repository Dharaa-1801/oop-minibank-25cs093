# Practical 8 — Custom exception hierarchy

Make banking failures explicit and informative.

## Requirements

- Add checked `BankException`.
- Add account-not-found, invalid-amount, and insufficient-funds exceptions.
- Replace failure flags with meaningful exceptions.
- Include useful context such as the account number or shortfall.
- Handle exceptions at the application boundary.

## Checkpoint

Invalid operations fail with a specific exception instead of a silent or
ambiguous result.

## Code

The exception implementation is in [`src/`](src/).
