package com.ricardo.balance_api.dto.event
import com.ricardo.balance_api.dto.BalanceDto

data class AccountInfoDto (
        val id: String,
        val owner: String,
        val createdAt: String,
        val status: String,
        val balance: BalanceDto
)