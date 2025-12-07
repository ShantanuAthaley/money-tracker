# Personal Finance — Money Tracker (Domain Description)

## Personal Finance Domain Overview

1. A `Household` is a collaborative financial unit shared by one or more Persons.

2. A Person has a unique identity expressed through their Name and Tax Identifier.

3. A Person owns one or more Accounts. Accounts may be individually owned or jointly owned by multiple persons.
4. An Account captures a customer’s relationship with a Financial Institution (e.g., bank).

5. Each Account Statement represents a periodic report containing a set of Transaction Records imported or ingested into the system.

6. The System provides a consolidated and historical view of a Household’s financial state.

## Domains & Aggregate Roots

`Each aggregate root defines a consistency boundary and is the authoritative entry point for modifications to its internal state.`

### 1. Household (Aggregate Root)**

    Represents a financial unit consisting of one or more Persons.
Holds references to Persons (by IDs), but Persons remain independent aggregates.

### 2. Person (Aggregate Root)

Represents an individual having a unique Name + Tax ID.
    
    Is responsible for managing personal identity and associations to Accounts.
(Accounts may reference Persons, but Persons do not contain Accounts directly.)

### 3. Account (Aggregate Root)

    Represents the ownership relationship between one or more Persons and a Financial Institution.
Responsible for holding Account-level metadata and the lifecycle of its Statements.

### 4. Statement (Aggregate Root)

    Represents an account statement for a specific time period (e.g., month). 
Contains a collection of Transaction Records as value objects.
All Transaction Records belong exclusively to this Statement aggregate.

## Value Objects

Value Objects are immutable, identity-less, equality-by-value constructs.

### TransactionRecord

Represents a single financial transaction recorded in a Statement.
Contains value objects such as:

#### 1. TransactionDate – the date the transaction was recorded by the bank.

##### 2. ValueDate – the date the transaction amount was actually applied/settled.

##### 3. Description – narrative description from the bank.

##### 4. Withdrawal – amount withdrawn (if applicable).

##### 5. Deposit – amount deposited (if applicable).

#### 6. Balance – running balance after the transaction.

##### 7. Currency – currency in which the transaction is denominated.