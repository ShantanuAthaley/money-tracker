package dev.shantanu.money.tracker.account.domain;

interface AccountQuery {
    record GetAccountResultByIdQuery(Long id) {
    }
}
