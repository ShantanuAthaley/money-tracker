package dev.shantanu.money.tracker.statement.domain;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionStagingRepository extends ListCrudRepository<StatementTransactionStaging, Integer> {
}
