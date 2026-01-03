package dev.shantanu.money.tracker.account.domain;

import dev.shantanu.money.tracker.account.AccountType;
import dev.shantanu.money.tracker.common.Ids;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

class AccountTest {

    static Long accountId;
    static AccountType accountType;
    static Set<Ids.PersonId> personIdSet;
    static String taxId;

    @BeforeAll
    static void setup() {
        accountId = 66L;
        accountType = AccountType.INDIVIDUAL;
        Ids.PersonId personId = new Ids.PersonId(56L);
        personIdSet = Set.of(personId);
        taxId = new String("XXXXX12345Y");
    }

    @Test
    void testAccountObject() {
        Account account = new Account(accountId, accountType, "Great bank",
                 taxId, null);

        Assertions.assertNotNull(account);
        Assertions.assertEquals(accountId, account.getAccountId());
        Assertions.assertEquals(66L, account.id());
        Assertions.assertEquals("XXXXX12345Y", account.getTaxId());
        Assertions.assertEquals(accountType, account.getAccountType());
        Assertions.assertEquals("Great bank", account.getBankName());
        Assertions.assertNotNull(account.getCreateDateTime());
        Assertions.assertNotNull(account.getUpdateDateTime());
    }
}