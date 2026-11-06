# Practical 4 — Validation, commands, and statements

Keep invalid user input out of the banking logic.

## Requirements

- Add compiled regular expressions for mobile, email, PAN, and IFSC.
- Create `TransactionType` with `DEPOSIT`, `WITHDRAW`, and `TRANSFER`.
- Create the `Command` record with transaction type, account number, and amount.
- Parse command text such as `DEPOSIT AC0001 500`.
- Build a multi-line account statement with `StringBuilder`.
- Demonstrate valid and invalid inputs and print a parsed command.

## Checkpoint

Bad input is rejected by validators and a valid command is converted to a
`Command` object.

## Code

The implementation for this practical is in [`src/`](src/).
