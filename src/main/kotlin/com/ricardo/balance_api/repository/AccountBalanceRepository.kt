package com.ricardo.balance_api.repository

import com.ricardo.balance_api.entity.AccountBalance
import org.springframework.data.jpa.repository.JpaRepository

interface AccountBalanceRepository :
        JpaRepository<AccountBalance, String>