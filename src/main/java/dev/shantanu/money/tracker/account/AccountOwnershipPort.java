package dev.shantanu.money.tracker.account;


import dev.shantanu.money.tracker.common.Ids;

import java.util.Map;

public interface AccountOwnershipPort {
    void createAccountOwnership(Ids.AccountId accountId, Map<Long, Double> accountOwners);
}
