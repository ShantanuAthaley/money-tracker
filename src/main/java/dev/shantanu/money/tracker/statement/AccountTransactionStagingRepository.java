package dev.shantanu.money.tracker.statement;

import dev.shantanu.money.tracker.common.Ids;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AccountTransactionStagingRepository extends ListCrudRepository<StatementTransactionStaging, Ids.TransactionId> {
}
