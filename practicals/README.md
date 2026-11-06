# MiniBank Practicals

Each practical is separated into its own folder so the work is easier to
follow. The practicals are cumulative: start with Practical 1 and continue in
order. Each folder now contains the Java code for that stage under `src/`.
Practical 14 contains the complete packaged application; the root
[`src/com/minibank`](../src/com/minibank) remains the main working copy.

| Practical | Main topic |
|---|---|
| [01](./practical-01) | Project setup and menu shell |
| [02](./practical-02) | Customer and Account classes |
| [03](./practical-03) | `toString`, equality, cloning, and nested classes |
| [04](./practical-04) | Validation, commands, and statements |
| [05](./practical-05) | Abstract accounts and polymorphism |
| [06](./practical-06) | Interfaces, packages, and runnable JAR |
| [07](./practical-07) | Annotations and reflection |
| [08](./practical-08) | Custom exception hierarchy |
| [09](./practical-09) | Threads and synchronization |
| [10](./practical-10) | Executor service and transaction processing |
| [11](./practical-11) | Serialization, logs, and reports |
| [12](./practical-12) | Collections and thread-safe storage |
| [13](./practical-13) | Generic repository |
| [14](./practical-14) | Final integration, tests, and packaging |

## How to use these folders

1. Read the practical README before coding.
2. Implement the requirements in the cumulative source tree.
3. Run the tests from the repository root.
4. Commit and tag the completed practical, for example:

For the complete separated project, run the Practical 14 build from its
folder:

```powershell
cd practical-14
& "C:\maven-mvnd\maven-mvnd\bin\mvnd.exe" clean test package
```

```text
git add .
git commit -m "Practical 01: menu shell"
git tag practical-01
```
