package com.ricardo.balance_api.dto

import org.hibernate.validator.constraints.Currency
import java.math.BigDecimal

data class BalanceDto(
        val amount: BigDecimal,
        val currency: String
)