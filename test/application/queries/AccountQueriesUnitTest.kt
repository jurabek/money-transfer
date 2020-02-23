package com.jurabek.application.queries

import application.toCurrency
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import domain.account.BankAccount
import domain.account.BankAccountRepository
import domain.account.Money
import org.junit.Test
import java.util.*

class AccountQueriesTest {
    @Test
    fun `when get allAccounts should return all account as dto objects`() {
        // arrange
        val account1 = BankAccount(UUID.randomUUID(), Money(100.toBigDecimal(), "EUR".toCurrency()))
        val account2 = BankAccount(UUID.randomUUID(), Money(200.toBigDecimal(), "EUR".toCurrency()))
        val account3 = BankAccount(UUID.randomUUID(), Money(300.toBigDecimal(), "EUR".toCurrency()))

        val bankAccountRepository = mock<BankAccountRepository> {
            on { getAll() } doReturn listOf(account1, account2, account3)
        }
        val accountQueries = AccountQueries(bankAccountRepository)

        // act

        // assert
    }
}