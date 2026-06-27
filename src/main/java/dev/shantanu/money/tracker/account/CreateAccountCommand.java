package dev.shantanu.money.tracker.account;

import java.util.Map;

public record CreateAccountCommand(AccountDetail accountDetail,
                                   Map<Long, Double> personOwnershipMap) {
}

