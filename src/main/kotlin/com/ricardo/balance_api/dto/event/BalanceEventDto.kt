package com.ricardo.balance_api.dto.event

data class BalanceEventDto (
        val transaction: TransactionDto,
        val account: AccountInfoDto
)