package com.ricardo.balance_api.dto
import java.time.OffsetDateTime
import  com.fasterxml.jackson.annotation.JsonProperty

data class AccountBalanceResponse (
        val id: String,
        val owner: String,
        val balance: BalanceDto,

        @JsonProperty("updated_At")
        val updatedAt: OffsetDateTime
)