# 🏦 MiniBank – Banking Management System

A Java-based console banking application designed to manage customer accounts, perform financial transactions, maintain transaction records, and generate banking reports.

Built using **Java 17**, **Object-Oriented Programming**, and advanced Java concepts.

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17">
  <img src="https://img.shields.io/badge/Maven-Build%20Tool-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven">
  <img src="https://img.shields.io/badge/JUnit-5-green?style=for-the-badge&logo=junit5&logoColor=white" alt="JUnit 5">
  <img src="https://img.shields.io/badge/Platform-Console-blue?style=for-the-badge" alt="Console Application">
</p>

---

## 📌 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Account Types](#-account-types)
- [Banking Operations](#-banking-operations)
- [Technology Stack](#-technology-stack)
- [Project Architecture](#-project-architecture)
- [Project Structure](#-project-structure)
- [OOP Concepts](#-object-oriented-programming-concepts)
- [Advanced Java Concepts](#-advanced-java-concepts)
- [Getting Started](#-getting-started)
- [Execution](#-execution)
- [Testing](#-testing)
- [Data Storage](#-data-storage)
- [Future Enhancements](#-future-enhancements)
- [License](#-license)

---

## 🚀 Overview

MiniBank is a menu-driven banking management system developed in Java.

It provides a simple way to manage multiple types of bank accounts and perform essential banking operations through a console interface.

The application demonstrates the practical implementation of software development concepts, including:

- Object-Oriented Programming
- Exception Handling
- Collections and Generics
- Custom Annotations
- Reflection
- File Handling
- Object Serialization
- Multithreading
- Unit Testing

The system also maintains transaction records and supports account data persistence using local files.

---

## ✨ Features

| Feature | Description |
|---|---|
| 🏦 Account Management | Create and manage different bank accounts |
| 💰 Deposits | Add money to an account |
| 💸 Withdrawals | Withdraw money with validation |
| 🔄 Fund Transfer | Transfer money between accounts |
| 📄 Account Statements | View account transaction history |
| 📋 Account Listing | Display available accounts |
| ⚙️ Batch Processing | Process deposit and withdrawal commands concurrently |
| 🧾 Audit Trail | View recorded transaction activity |
| 🧾 Transaction Logging | Maintain transaction records |
| 📊 Daily Reports | Generate transaction reports |
| 💾 Data Persistence | Save and load account information |
| ⚠️ Exception Handling | Handle invalid operations safely |
| 🧵 Multithreading | Process concurrent tasks |
| 🧪 Unit Testing | Test banking service functionality |

---

## 🏦 Account Types

The system supports three types of bank accounts.

<details>
<summary><b>1. Savings Account</b></summary>

- Designed for regular savings.
- Supports deposits and withdrawals.
- Implements account-specific withdrawal rules.

</details>

<details>
<summary><b>2. Current Account</b></summary>

- Designed for frequent transactions.
- Supports deposits and withdrawals.
- Uses account-specific transaction rules.

</details>

<details>
<summary><b>3. Fixed Deposit Account</b></summary>

- Designed for fixed-term deposits.
- Supports account-specific interest-related behavior.
- Uses specialized account rules.

</details>

---

## 💳 Banking Operations

The application supports the following operations:

1. Create a new account.
2. Deposit money.
3. Withdraw money.
4. Transfer funds between accounts.
5. View account statements.
6. Display all accounts.
7. Generate daily transaction reports.
8. Save account information.
9. Load previously saved account information.

---

## 🛠️ Technology Stack

| Technology | Usage |
|---|---|
| Java 17 | Core programming language |
| Apache Maven | Project build and dependency management |
| JUnit 5 | Unit testing |
| Java Collections Framework | Data management |
| Java Serialization | Object persistence |
| ExecutorService | Thread management |
| Git | Version control |
| GitHub | Source code hosting |

---

## 🏗️ Project Architecture

The application follows a modular package-based architecture.

```text
                 ┌──────────────────────┐
                 │       MiniBank       │
                 │   Console Interface  │
                 └──────────┬───────────┘
                            │
                 ┌──────────▼───────────┐
                 │     BankService      │
                 │   Banking Operations│
                 └──────────┬───────────┘
                            │
             ┌──────────────┼──────────────┐
             │              │              │
      ┌──────▼─────┐ ┌──────▼──────┐ ┌─────▼──────┐
      │   Model    │ │    Util     │ │  Exception │
      │   Package  │ │   Package   │ │   Package  │
      └────────────┘ └─────────────┘ └────────────┘
                            │
                 ┌──────────▼───────────┐
                 │    Data Storage      │
                 │ Serialization / Logs │
                 └──────────────────────┘
```

---

## 📂 Project Structure

```text
oop-minibank-25CS093/
│
├── .devcontainer/
│   └── devcontainer.json
│
├── data/
│   ├── accounts.dat
│   ├── daily-report.txt
│   ├── transactions.log
│   └── logs/
│       └── transactions.log
│
├── src/
│   │
│   ├── exception/
│   │   ├── AccountNotFoundException.java
│   │   ├── BankException.java
│   │   ├── InsufficientFundsException.java
│   │   └── InvalidAmountException.java
│   │
│   ├── model/
│   │   ├── annotation/
│   │   │   ├── Id.java
│   │   │   ├── MaxLength.java
│   │   │   └── Positive.java
│   │   │
│   │   ├── Account.java
│   │   ├── Customer.java
│   │   ├── CurrentAccount.java
│   │   ├── FixedDepositAccount.java
│   │   ├── InterestBearing.java
│   │   ├── Premium.java
│   │   ├── SavingsAccount.java
│   │   ├── Transactable.java
│   │   └── WithdrawRule.java
│   │
│   ├── service/
│   │   ├── AccountStore.java
│   │   ├── AccountWorker.java
│   │   ├── BankInfo.java
│   │   ├── BankService.java
│   │   ├── BankSession.java
│   │   ├── MenuOption.java
│   │   ├── MiniBank.java
│   │   ├── TransactionBuffer.java
│   │   └── TransactionProcessor.java
│   │
│   ├── test/
│   │   └── java/
│   │       └── service/
│   │           └── BankServiceTest.java
│   │
│   └── util/
│       ├── AnnotationValidator.java
│       ├── Command.java
│       ├── CommandParser.java
│       ├── ReportGenerator.java
│       ├── Repository.java
│       ├── StatementFormatter.java
│       ├── StatePersister.java
│       ├── TransactionLog.java
│       ├── TransactionType.java
│       └── Validator.java
│
├── .gitignore
├── pom.xml
└── README.md
```

### 📦 Package Responsibilities

<details>
<summary><b>model</b></summary>

Contains account-related classes, customer information, account types, interfaces and custom annotations.

</details>

<details>
<summary><b>service</b></summary>

Contains the main banking business logic, account management, session handling and transaction processing.

</details>

<details>
<summary><b>exception</b></summary>

Contains custom exceptions for banking-related errors.

</details>

<details>
<summary><b>util</b></summary>

Contains validation, command parsing, transaction logging, report generation, persistence and reusable helper components.

</details>

<details>
<summary><b>test</b></summary>

Contains unit tests for validating banking service functionality.

</details>

---

## 🧠 Object-Oriented Programming Concepts

| Concept | Implementation |
|---|---|
| Encapsulation | Controlled access to account data |
| Inheritance | Specialized account classes |
| Polymorphism | Account-specific method implementations |
| Abstraction | Abstract classes and interfaces |
| Interfaces | Common banking behavior |
| Exception Handling | Custom banking exceptions |

---

## ⚙️ Advanced Java Concepts

<details>
<summary><b>Generics</b></summary>

Used to create reusable and type-safe components.

</details>

<details>
<summary><b>Collections Framework</b></summary>

Used to organize and manage account and transaction data.

</details>

<details>
<summary><b>Custom Annotations</b></summary>

Custom annotations such as `@Id`, `@MaxLength` and `@Positive` are used for validation-related metadata.

</details>

<details>
<summary><b>Reflection API</b></summary>

Used to inspect annotation metadata at runtime.

</details>

<details>
<summary><b>Serialization</b></summary>

Used to store and retrieve account objects from local files.

</details>

<details>
<summary><b>Multithreading</b></summary>

Uses Java concurrency utilities, synchronization and transaction processing components.

</details>

---

## 💻 Getting Started

### Prerequisites

Install the following:

- Java Development Kit (JDK 17)
- Apache Maven
- Git
- VS Code / IntelliJ IDEA / Eclipse

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

---

## 📥 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/Dharaa-1801/oop-minibank-25cs093.git
```

### 2. Navigate to the Project

```bash
cd oop-minibank-25cs093
```

### 3. Build the Project

```bash
mvn clean package
```

### 4. Run Unit Tests

```bash
mvn test
```

---

## ▶️ Execution

Run the application using:

```bash
java -jar target/oop-minibank-25cs093-1.0.jar
```

The console-based banking menu will start.

> **Note:** The JAR filename must match the artifact name and version configured in `pom.xml`.

---

## 🧪 Testing

JUnit 5 is used for unit testing.

Test source:

```text
src/test/java/service/BankServiceTest.java
```

Execute tests:

```bash
mvn test
```

---

## 💾 Data Storage

The application maintains its runtime data in the `data/` directory.

| File | Purpose |
|---|---|
| `accounts.dat` | Serialized account information |
| `transactions.log` | Transaction records |
| `logs/transactions.log` | Transaction logging |
| `daily-report.txt` | Generated daily report |

---

### Main Menu

<!-- Add your main menu screenshot here -->

### Account Creation

<!-- Add your account creation screenshot here -->

### Deposit and Withdrawal

<!-- Add your deposit and withdrawal screenshots here -->

### Fund Transfer

<!-- Add your fund transfer screenshot here -->

### Account Statement

<!-- Add your account statement screenshot here -->

### Daily Transaction Report

<!-- Add your report screenshot here -->

---

## 🔮 Future Enhancements

- Graphical User Interface (GUI).
- Database integration.
- Secure login and authentication.
- Role-based access control.
- PDF account statement generation.
- Email and SMS notifications.
- Online banking support.

---

## 📚 Learning Outcomes

This project provides practical experience in:

- Designing modular Java applications.
- Applying Object-Oriented Programming principles.
- Implementing banking business logic.
- Handling exceptions and validating data.
- Managing files and serialized objects.
- Understanding concurrent programming.
- Writing and executing unit tests.
- Using Maven and Git for project management.

---

## 📄 License

This project is intended for educational and learning purposes.

---

<p align="center">
  <b>MiniBank – Simple Banking, Smarter Management 🏦</b>
</p>