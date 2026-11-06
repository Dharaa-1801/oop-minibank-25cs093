# Practical 13 — Generic repository

Make storage reusable for any identified model type.

## Requirements

- Create `Repository<T>` backed by a map.
- Find each object’s key through its `@Id` field using reflection.
- Add save, find, remove, and list operations.
- Add `saveAll(T... items)` with `@SafeVarargs`.
- Add a bounded generic method.
- Reuse the repository with accounts and another model type.

## Checkpoint

One type-safe repository stores multiple model types without duplicated
storage logic.

## Code

The generic repository implementation is in [`src/`](src/).
