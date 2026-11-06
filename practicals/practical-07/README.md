# Practical 7 — Annotations and reflection

Add reusable metadata-driven validation.

## Requirements

- Add `@Id`, `@Positive`, and `@MaxLength`.
- Apply `@Override` and `@FunctionalInterface` where appropriate.
- Define runtime retention and valid targets for custom annotations.
- Read fields and annotations with reflection.
- Validate annotated objects without hard-coding every class.

## Checkpoint

Annotated fields are discovered and validation failures identify the field.

## Code

The annotation implementation is in [`src/`](src/).
