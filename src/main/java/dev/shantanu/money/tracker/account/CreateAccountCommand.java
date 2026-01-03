package dev.shantanu.money.tracker.account;

import java.util.Set;

public record CreateAccountCommand(AccountDetail accountDetail,
                                   Set<AccountOwnership> accountOwners) {
}

