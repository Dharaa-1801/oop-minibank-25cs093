# Practical 11 — Persistence, logs, and reports

Save bank state and produce an end-of-day report.

## Requirements

- Make accounts, subclasses, and customers `Serializable`.
- Save and load account state from a file.
- Append transactions to a log.
- Use NIO to walk log files.
- Generate an end-of-day summary report.
- Load state at startup and save it on exit.

## Checkpoint

Restarting the application preserves account state and the report summarizes
logged activity.

## Code

The persistence, logging, and reporting implementation is in [`src/`](src/).
