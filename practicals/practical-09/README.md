# Practical 9 — Threads and synchronization

Protect account state when multiple users act at the same time.

## Requirements

- Create an `AccountWorker` implementing `Runnable`.
- Run repeated deposits and withdrawals from multiple threads.
- Demonstrate the race condition with an unsafe account.
- Make account operations thread-safe with synchronization.
- Join worker threads before reading the final balance.

## Code

The worker implementation is in [`src/`](src/).

## Checkpoint

The synchronized implementation produces a deterministic final balance.
