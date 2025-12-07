package dev.shantanu.money.tracker.person;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("person_account")
class PersonAccount {

    // no @Id because it's part of the mapped collection key (composite PK in DB)
    @Column("account_id")
    private Long accountId;

    @Column("owning_percentage")
    private java.math.BigDecimal owningPercentage;

    public PersonAccount(Long accountId, java.math.BigDecimal owningPercentage) {
        this.accountId = accountId;
        this.owningPercentage = owningPercentage;
    }

    // getters
    public Long getAccountId() { return accountId; }
    public java.math.BigDecimal getOwningPercentage() { return owningPercentage; }
}
