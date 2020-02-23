package application.queries

import application.dtos.account.AccountBalanceDto
import application.dtos.account.AccountDto
import application.toCurrency
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import domain.account.BankAccount
import domain.account.BankAccountRepository
import domain.account.Money
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.util.*

class AccountQueriesUnitTest {
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
        val result = accountQueries.getAllAccounts()
        val resultAccount1 = result[0]
        val resultAccount2 = result[1]
        val resultAccount3 = result[2]

        // assert
        assertThat(result, hasItem(instanceOf(AccountDto::class.java)))
        assertThat(result.size, equalTo(3))

        assertThat(resultAccount1.balance, equalTo(account1.balance.amount))
        assertThat(resultAccount2.balance, equalTo(account2.balance.amount))
        assertThat(resultAccount3.balance, equalTo(account3.balance.amount))

        assertThat(resultAccount1.currency, equalTo(account1.balance.currency))
        assertThat(resultAccount2.currency, equalTo(account2.balance.currency))
        assertThat(resultAccount3.currency, equalTo(account3.balance.currency))

        assertThat(resultAccount1.id, equalTo(account1.id))
        assertThat(resultAccount2.id, equalTo(account2.id))
        assertThat(resultAccount3.id, equalTo(account3.id))
    }

    @Test
    fun `when passed account id should return current account balance`() {
        // arrange
        val account1 = BankAccount(UUID.randomUUID(), Money(100.toBigDecimal(), "EUR".toCurrency()))

        val bankAccountRepository = mock<BankAccountRepository> {
            on { getById(account1.id) } doReturn account1
        }
        val accountQueries = AccountQueries(bankAccountRepository)

        // act
        val result = accountQueries.getAccountBalanceById(account1.id)

        // assert
        assertThat(result, instanceOf(AccountBalanceDto::class.java))
        assertThat(result.amount, equalTo(account1.balance.amount))
        assertThat(result.currency, equalTo(account1.balance.currency.toString()))
    }
}