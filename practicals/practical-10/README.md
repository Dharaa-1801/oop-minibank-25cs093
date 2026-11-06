# Practical 10 — Transaction processing

Build a transaction engine for concurrent batches.

## Requirements

- Create `TransactionProcessor` with a fixed thread pool.
- Submit a batch of transactions and wait for completion.
- Coordinate producer and consumer work with `wait` and `notify`.
- Avoid deadlock by using consistent lock ordering.
- Shut down the executor cleanly.

## Checkpoint

A transaction batch completes safely without leaked worker threads or
deadlocks.

## Code

The transaction processor implementation is in [`src/`](src/).
