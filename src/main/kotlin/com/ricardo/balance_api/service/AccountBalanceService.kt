package com.ricardo.balance_api.service

import com.ricardo.balance_api.entity.AccountBalance
import com.ricardo.balance_api.repository.AccountBalanceRepository
import org.springframework.stereotype.Service
import com.ricardo.balance_api.dto.event.BalanceEventDto
import java.time.OffsetDateTime
import org.slf4j.LoggerFactory

@Service
class AccountBalanceService(
        private val repository: AccountBalanceRepository
) {
    private val log =
            LoggerFactory.getLogger(AccountBalanceService::class.java)
    fun countAccounts(): Long {
        return repository.count()
    }
    fun createTestAccount() {
        val account = AccountBalance(
                accountId = "123-456-789",
                owner = "315e3cfe-f4af-12321f-df4ddd-",
                accountStatus ="ENABLED",
                amount = java.math.BigDecimal("1000.00"),
                currency = "BRL",
                lastTransactionId = "TX001",
                lastTransactionType = "CREDIT",
                lastTransactionStatus = "APPROVED",
                lastTransactionTimestamp = 123456789L,
                accountCreatedAt = 1634874339L,
                updatedAt = java.time.OffsetDateTime.now()
        )
        repository.save(account)
    }
    fun getBalanceByAccountId(accountId: String):
            AccountBalance? {
        return repository.findById(accountId).orElse(null)
    }
    fun processBalanceEvents(event: BalanceEventDto):
            AccountBalance {
        log.info(
                "Processando evento da conta{} - timestamp {}",
                event.account.id,
                event.transaction.timestamp
        )
        val existingAccount = repository.findById(event.account.id).orElse(null)

        if (existingAccount!= null && event.transaction.timestamp <= existingAccount.lastTransactionTimestamp) {
            log.info(
                    "Evento ignorado. Conta {} possui cadastro de TIMESTAMP (BD: {} ) mais recente que o recebido no evento ({})",
                    event.account.id,
                    existingAccount.lastTransactionTimestamp,
                    event.transaction.timestamp
            )
            return existingAccount
        }
        val accountBalance = AccountBalance(
                accountId = event.account.id,
                owner = event.account.owner,
                accountStatus = event.account.status,
                accountCreatedAt = event.account.createdAt.toLong(),
                amount = event.account.balance.amount,
                currency = event.account.balance.currency,
                lastTransactionId = event.transaction.id,
                lastTransactionType = event.transaction.type,
                lastTransactionStatus = event.transaction.status,
                lastTransactionTimestamp = event.transaction.timestamp,
                updatedAt = OffsetDateTime.now()
        )
        if (existingAccount == null) {
            log.info(
                    "Criando a conta {} com saldo de {}",
                    event.account.id,
                    event.account.balance.amount
            )
        } else {
            log.info(
                    "Atualizando saldo da conta {} de {} para {}",
                    event.account.id,
                    existingAccount.amount,
                    event.account.balance.amount
            )
        }
        return repository.save(accountBalance)
    }
}
