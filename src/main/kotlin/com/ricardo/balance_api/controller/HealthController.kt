package com.ricardo.balance_api.controller

import com.ricardo.balance_api.dto.AccountBalanceResponse
import com.ricardo.balance_api.dto.BalanceDto
import com.ricardo.balance_api.entity.AccountBalance
import org.springframework.web.bind.annotation.PathVariable
import com.ricardo.balance_api.service.AccountBalanceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import com.ricardo.balance_api.dto.event.BalanceEventDto
import org.springframework.http.HttpStatus

@RestController
class HealthController(
        private val accountBalanceService: AccountBalanceService
) {
    @GetMapping("/hello")
    fun hello(): Map<String, String> {
        return mapOf(
                "message" to "API funcionando"
        )
    }

    @GetMapping("/accounts/count")
    fun countAccounts(): Map<String, Long> {
        return mapOf("totalAccounts" to
                accountBalanceService.countAccounts())
    }

    @GetMapping("/accounts/create")
    fun createAccount(): Map<String, String> {
        accountBalanceService.createTestAccount()
        return mapOf("message" to "Conta criada")
    }

    @GetMapping("/balances/{accountId}")
    fun getBalance(
            @PathVariable accountId: String
    ): ResponseEntity<Any>? {
        val account = accountBalanceService.getBalanceByAccountId(accountId)

        if (account == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body((mapOf("message" to "Conta-inexistente")))
        }

        val response = AccountBalanceResponse(
                id = account.accountId,
                owner = account.owner,
                balance = BalanceDto(
                        amount = account.amount,
                        currency = account.currency
                ),
                updatedAt = account.updatedAt
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping("balances/events")
    fun processEvent(
            @RequestBody event: BalanceEventDto
    ):Map<String, String> {
        accountBalanceService.processBalanceEvents(event)
        return mapOf("message" to "Evento processado"
        )
    }
}