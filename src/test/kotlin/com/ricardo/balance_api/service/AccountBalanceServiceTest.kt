package com.ricardo.balance_api.service

import com.ricardo.balance_api.repository.AccountBalanceRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.Optional
import org.junit.jupiter.api.Assertions.assertNull
import com.ricardo.balance_api.entity.AccountBalance
import com.ricardo.balance_api.dto.BalanceDto
import com.ricardo.balance_api.dto.event.BalanceEventDto
import com.ricardo.balance_api.dto.event.TransactionDto
import com.ricardo.balance_api.dto.event.AccountInfoDto
import java.sql.DriverManager.println

class AccountBalanceServiceTest {
    //private val repository = mock(AccountBalanceRepository::class.java)
    private val repository: AccountBalanceRepository = mock(AccountBalanceRepository::class.java)
    private val service = AccountBalanceService(repository)

    @Test
    fun nullSeContaNaoExiste() {
        `when`(repository.findById("conta-inexistente")).thenReturn(Optional.empty())


    val result = service.getBalanceByAccountId("conta-inexistente")

    assertNull(result)

    }

    @Test
    fun eventoAntigoNaoAtualizaSaldo() {
        val existingAccount = AccountBalance(
                accountId = "123",
                owner = "owner-1",
                accountStatus = "ENABLED",
                accountCreatedAt = 10001,
                amount = java.math.BigDecimal("7000.00"),
                currency = "BRL",
                lastTransactionId = "TX300",
                lastTransactionType = "CREDIT",
                lastTransactionStatus = "APPROVED",
                lastTransactionTimestamp = 300L,
                updatedAt = java.time.OffsetDateTime.now()

        )
        val event = BalanceEventDto(
                transaction = TransactionDto(
                        id = "TX100",
                        type = "CREDIT",
                        amount = java.math.BigDecimal("9999.00"),
                        currency ="BRL",
                        status = "APPROVED",
                        timestamp = 100L
                ),
                account = AccountInfoDto(
                        id = "123",
                        owner = "owner-1",
                        createdAt = "1000",
                        status = "ENABLED",
                        balance = BalanceDto(
                                amount = java.math.BigDecimal("9999.00"),
                                currency = "BRL"
                        )
                )
        )
        `when`(repository.findById("123"))
                .thenReturn(Optional.of(existingAccount))
        val result = service.processBalanceEvents(event)
        println("Print" )
        println(result.toString())
        org.junit.jupiter.api.Assertions.assertEquals(
                java.math.BigDecimal("7000.00"),
                result.amount
        )
        org.mockito.Mockito.verify(
                repository,
                org.mockito.Mockito.never()
        ).save(
                org.mockito.ArgumentMatchers.any()
                )
    }

    @Test
    fun eventoNovoAtualizaSaldo() {
        val existingAccount = AccountBalance(
                accountId = "123",
                owner = "owner-1",
                accountStatus = "ENABLED",
                accountCreatedAt = 10001,
                amount = java.math.BigDecimal("7000.00"),
                currency = "BRL",
                lastTransactionId = "TX300",
                lastTransactionType = "CREDIT",
                lastTransactionStatus = "APPROVED",
                lastTransactionTimestamp = 300L,
                updatedAt = java.time.OffsetDateTime.now()

        )
        val event = BalanceEventDto(
                transaction = TransactionDto(
                        id = "TX100",
                        type = "CREDIT",
                        amount = java.math.BigDecimal("9999.00"),
                        currency ="BRL",
                        status = "APPROVED",
                        timestamp = 500L
                ),
                account = AccountInfoDto(
                        id = "123",
                        owner = "owner-1",
                        createdAt = "1000",
                        status = "ENABLED",
                        balance = BalanceDto(
                                amount = java.math.BigDecimal("9999.00"),
                                currency = "BRL"
                        )
                )
        )
        `when`(repository.findById("123"))
                .thenReturn(Optional.of(existingAccount))
        `when`(repository.save(org.mockito.ArgumentMatchers.any()))
                .thenAnswer{ it.arguments[0] }
        val result = service.processBalanceEvents(event)
        println("Print" )
        println(result.toString())
        org.junit.jupiter.api.Assertions.assertEquals(
                java.math.BigDecimal("9999.00"),
                result.amount
        )
    }
}
