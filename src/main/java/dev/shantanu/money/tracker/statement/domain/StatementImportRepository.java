package dev.shantanu.money.tracker.statement.domain;

import dev.shantanu.money.tracker.common.Ids.StatementImportId;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface StatementImportRepository extends ListCrudRepository<StatementImport, StatementImportId> {
}
