-- =========================
-- Create schema
-- =========================
CREATE SCHEMA IF NOT EXISTS money_tracker;


-- =========================
-- TABLE: household
-- =========================
CREATE TABLE IF NOT EXISTS money_tracker.household (
    household_id       BIGSERIAL PRIMARY KEY,
    name               VARCHAR(50) NOT NULL,
    address            TEXT,
    created_datetime   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_datetime   TIMESTAMP
);


-- =========================
-- TABLE: person
-- =========================
CREATE TABLE IF NOT EXISTS money_tracker.person (
    person_id          BIGSERIAL PRIMARY KEY,
    household_id       BIGINT REFERENCES money_tracker.household(household_id),
    name               VARCHAR(50) NOT NULL,
    tax_id             VARCHAR(20),
    email              VARCHAR(200),
    date_of_birth      DATE,
    created_datetime   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_datetime   TIMESTAMP
);


-- =========================
-- TABLE: account
-- =========================
CREATE TABLE IF NOT EXISTS money_tracker.account (
    account_id         BIGSERIAL PRIMARY KEY,
    account_type       VARCHAR(50) NOT NULL,
    account_holder     TEXT,
    bank_name          VARCHAR(56),
    tax_id             VARCHAR(20),
    created_datetime   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_datetime   TIMESTAMP
);


-- =========================
-- TABLE: person_account
-- =========================
CREATE TABLE IF NOT EXISTS money_tracker.person_account (
    person_id          BIGINT NOT NULL REFERENCES money_tracker.person(person_id),
    account_id         BIGINT NOT NULL REFERENCES money_tracker.account(account_id),
    owning_percentage  NUMERIC(5,2),
    PRIMARY KEY (person_id, account_id)
);


-- =========================
-- TABLE: ingress_statement
-- =========================
CREATE TABLE IF NOT EXISTS money_tracker.ingress_statement (
    id                      BIGSERIAL PRIMARY KEY,
    import_date             TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    statement_file          TEXT NOT NULL,

    account_id              BIGINT NOT NULL REFERENCES money_tracker.account(account_id),
    account_type            VARCHAR(50) NOT NULL,
    statement_type          VARCHAR(50) NOT NULL,

    transaction_date_from   DATE,
    transaction_date_to     DATE,
    opening_balance         NUMERIC(18, 2),
    closing_balance         NUMERIC(18, 2),

    parsing_errors          JSONB,
    created_datetime        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_datetime        TIMESTAMP
);


-- =========================
-- TABLE: transaction_staging
-- =========================
CREATE TABLE IF NOT EXISTS money_tracker.transaction_staging (
    staging_id              BIGSERIAL PRIMARY KEY,
    ingress_id              BIGINT NOT NULL REFERENCES money_tracker.ingress_statement(id),
    account_id              BIGINT NOT NULL REFERENCES money_tracker.account(account_id),

    transaction_date        DATE NOT NULL,
    value_date              DATE,
    description             TEXT,
    check_number            VARCHAR(20),
    withdrawal_amount       DECIMAL,
    deposit_amount          DECIMAL,
    closing_balance         DECIMAL,

    parsing_errors          JSONB,

    dedupe_key_hash         TEXT,
    created_datetime        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- =========================
-- TABLE: transaction_record
-- =========================
CREATE TABLE IF NOT EXISTS money_tracker.transaction_record (
    id                      BIGSERIAL PRIMARY KEY,
    account_id              BIGINT NOT NULL REFERENCES money_tracker.account(account_id),

    transaction_date        DATE NOT NULL,
    value_date              DATE,
    description             TEXT,
    check_number            VARCHAR(20),
    withdrawal              DECIMAL,
    deposit                 DECIMAL,
    balance                 DECIMAL,

    dedupe_key_hash         TEXT NOT NULL,
    created_datetime        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- =========================
-- Indexes
-- =========================

CREATE INDEX IF NOT EXISTS idx_ingress_account_id
    ON money_tracker.ingress_statement(account_id);

CREATE INDEX IF NOT EXISTS idx_ingress_import_date
    ON money_tracker.ingress_statement(import_date);

CREATE INDEX IF NOT EXISTS idx_ingress_statement_type
    ON money_tracker.ingress_statement(statement_type);

CREATE INDEX IF NOT EXISTS idx_ingress_txn_range
    ON money_tracker.ingress_statement(transaction_date_from, transaction_date_to);

-- staging index (corrected column name)
CREATE INDEX IF NOT EXISTS idx_staging_account_date
    ON money_tracker.transaction_staging(account_id, transaction_date);

-- transaction record dedupe index
CREATE UNIQUE INDEX IF NOT EXISTS uq_transaction_dedupe
    ON money_tracker.transaction_record(dedupe_key_hash);

CREATE INDEX IF NOT EXISTS idx_transaction_lookup
    ON money_tracker.transaction_record(account_id, transaction_date, balance);


-- =========================
-- Constraints
-- =========================

ALTER TABLE money_tracker.account
    ADD CONSTRAINT uq_account_tax_id UNIQUE (tax_id);

ALTER TABLE money_tracker.account
    ADD CONSTRAINT chk_account_type_nonempty CHECK (account_type <> '');

ALTER TABLE money_tracker.ingress_statement
    ADD CONSTRAINT chk_transaction_date_range
        CHECK (
            transaction_date_from IS NULL OR
            transaction_date_to IS NULL OR
            transaction_date_from <= transaction_date_to
        );

ALTER TABLE money_tracker.ingress_statement
    ADD CONSTRAINT chk_opening_balance_non_negative CHECK (opening_balance IS NULL OR opening_balance >= 0);

ALTER TABLE money_tracker.ingress_statement
    ADD CONSTRAINT chk_closing_balance_non_negative CHECK (closing_balance IS NULL OR closing_balance >= 0);
