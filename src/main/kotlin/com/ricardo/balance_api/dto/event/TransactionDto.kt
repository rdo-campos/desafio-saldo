package com.ricardo.balance_api.dto.event

import java.math.BigDecimal

data class TransactionDto (
        val id: String,
        val type: String,
        val amount: BigDecimal,
        val currency: String,
        val status: String,
        val timestamp: Long
)