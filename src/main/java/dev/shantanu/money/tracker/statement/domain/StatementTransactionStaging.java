package dev.shantanu.money.tracker.statement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table("transaction_staging")
record StatementTransactionStaging(Long stagingId,
                                   Long ingressId,
                                   @Column("parsing_errors")
                          @JsonFormat(shape = JsonFormat.Shape.STRING)
                          JSONObject parsingErrors,
                                   String dedupeKeyHash,
                                   @Column("created_datetime")
                          @Embedded(onEmpty = Embedded.OnEmpty.USE_EMPTY)
                          Transaction transaction,
                                   LocalDateTime createdDateTime
) {
}


record Transaction(Long accountId,
                   LocalDate transactionDate,
                   LocalDate valueDate,
                   String description,
                   String checkNumber,
                   Double withdrawalAmount,
                   Double depositAmount,
                   Double closingBalance,
                   String dedupeKeyHash) {
}