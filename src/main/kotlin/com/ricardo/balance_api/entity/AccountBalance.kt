package com.ricardo.balance_api.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity
@Table(name = "account_balance")
data class AccountBalance(

        @Id
        val accountId: String,

        val owner: String,

        val accountStatus: String,

        val accountCreatedAt: Long,

        val amount: BigDecimal,

        val currency: String,

        val lastTransactionId: String,

        val lastTransactionType: String,

        val lastTransactionStatus: String,

        val lastTransactionTimestamp: Long,

        val updatedAt: OffsetDateTime
)