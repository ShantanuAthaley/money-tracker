package dev.shantanu.money.tracker.statement.domain;

import dev.shantanu.money.tracker.common.Ids.AccountId;
import dev.shantanu.money.tracker.common.Ids.TransactionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.xml.crypto.dsig.DigestMethod;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.Objects;

@Table("transaction_record")
class TransactionRecord {
    private static final Logger LOGGER = LoggerFactory.getLogger(Transaction.class);
    @Id
    private TransactionId id;

    private AggregateReference<AccountId, AccountId> accountId;

    private final LocalDate transactionDate;
    private final LocalDate valueDate;
    private final String description;
    private final String category;

    private final java.math.BigDecimal withdrawal;
    private final java.math.BigDecimal deposit;
    private final java.math.BigDecimal balance;
    @Column("dedupe_key_hash")
    private final String transactionHash;

    public TransactionRecord(TransactionId id, AggregateReference<AccountId, AccountId> accountId, LocalDate transactionDate, LocalDate valueDate, String description, String category, BigDecimal withdrawal, BigDecimal deposit, BigDecimal balance, String transactionHash) {
        this.id = id;
        this.accountId = accountId;
        this.transactionDate = transactionDate;
        this.valueDate = valueDate;
        this.description = description;
        this.category = category;
        this.withdrawal = withdrawal;
        this.deposit = deposit;
        this.balance = balance;
        this.transactionHash = computeTransactionHash(accountId.getId(), transactionDate, withdrawal, deposit, balance);
    }

    private String computeTransactionHash(AccountId id, LocalDate transactionDate, BigDecimal withdrawal, BigDecimal deposit, BigDecimal balance) {
        try {
            final String inputStr = accountId + "|" + transactionDate + "|" + withdrawal + "|" + deposit + "|" + balance;
            MessageDigest instance = MessageDigest.getInstance(DigestMethod.SHA3_256);
            byte[] digest = instance.digest(inputStr.getBytes());
            return new String(digest, Charset.defaultCharset());
        } catch (Exception e) {
            LOGGER.error("Error calculating transactionHash - search key.");
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        TransactionRecord that = (TransactionRecord) o;
        return Objects.equals(id, that.id)
                && Objects.equals(accountId, that.accountId)
                && Objects.equals(transactionDate, that.transactionDate)
                && Objects.equals(valueDate, that.valueDate)
                && Objects.equals(description, that.description)
                && Objects.equals(category, that.category)
                && Objects.equals(withdrawal, that.withdrawal)
                && Objects.equals(deposit, that.deposit)
                && Objects.equals(balance, that.balance)
                && Objects.equals(transactionHash, that.transactionHash);

    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(accountId);
        result = 31 * result + Objects.hashCode(transactionDate);
        result = 31 * result + Objects.hashCode(valueDate);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(category);
        result = 31 * result + Objects.hashCode(withdrawal);
        result = 31 * result + Objects.hashCode(deposit);
        result = 31 * result + Objects.hashCode(balance);
        result = 31 * result + Objects.hashCode(transactionHash);
        return result;
    }
}
