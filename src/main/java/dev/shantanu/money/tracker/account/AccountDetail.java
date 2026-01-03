package dev.shantanu.money.tracker.account;

public record AccountDetail(Long accountId,
                            AccountType accountType,
                            String bankName,
                            String taxId){
}
