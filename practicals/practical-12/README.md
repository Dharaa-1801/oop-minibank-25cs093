# Practical 12 — Collections and thread-safe storage

Replace fixed arrays with searchable, scalable collections.

## Requirements

- Create an `AccountStore` backed by `HashMap<String, Account>`.
- Use account number as the map key.
- Add, find, remove, and list operations.
- Sort accounts with a `Comparator`.
- Provide a thread-safe map for concurrent access.

## Checkpoint

Accounts can be added, found, sorted, and accessed safely from concurrent
operations.

## Code

The collection store implementation is in [`src/`](src/).
