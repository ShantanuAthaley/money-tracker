package dev.shantanu.money.tracker.account;

import dev.shantanu.money.tracker.common.Ids;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AccountRepository extends ListCrudRepository<Account, Ids.AccountId> {
}
