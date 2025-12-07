package dev.shantanu.money.tracker.statement;

import dev.shantanu.bankstatement.common.TransactionRecord;
import dev.shantanu.money.tracker.common.Ids;
import org.springframework.data.repository.ListCrudRepository;

interface TransactionRecordRepository extends ListCrudRepository<TransactionRecord, Ids.TransactionId> {
}
