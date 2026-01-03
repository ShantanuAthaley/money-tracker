package dev.shantanu.money.tracker.account.domain;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface AccountRepository extends ListCrudRepository<Account, Long> {
    @Override @NotNull @Query("SELECT a.* FROM money_tracker.account a where a.account_id = :accountId")
    Optional<Account> findById(@Param("accountId") @NotNull Long accountId);
}
