package domain

import domain.account.BankAccount
import domain.account.Money
import domain.events.MoneyCredited
import domain.events.MoneyDebited
import org.hamcrest.CoreMatchers.*
import org.hamcrest.core.*
import org.junit.Assert
import org.junit.Test
import java.util.*

class BankAccountTest {
    @Test
    fun `Account balance must be computed as a Money instance`() {
        // arrange
        val account = createFakeAccount()

        // act
        val result = account.balance

        // assert
        Assert.assertThat(result, Is.isA(Money::class.java))
        Assert.assertThat(result.amount, equalTo(account.balance.amount))
        Assert.assertThat(result.currency, equalTo(account.balance.currency))
    }

    @Test(expected = DomainException::class)
    fun `Credit should throw Domain exception when available balance less than crediting amount`() {
        // arrange
        val account = createFakeAccount()

        // act
        val result = account.credit(Money(300.toBigDecimal(), Currency.getInstance("EUR")))
    }

    @Test
    fun `Credit should subtract from actual balance and create new instance`() {
        // arrange
        val account = createFakeAccount()
        val money = Money(100.toBigDecimal(), Currency.getInstance("EUR"))

        // act
        val result = account.credit(money)

        // assert
        Assert.assertThat(result, IsNot.not(sameInstance(account)))
        Assert.assertThat(result.balance.amount, equalTo(100.toBigDecimal()))
    }

    @Test
    fun `Debit should add to actual balance and create new instance`() {
        // arrange
        val account = createFakeAccount()
        val money = Money(100.toBigDecimal(), Currency.getInstance("EUR"))

        // act
        val result = account.debit(money)

        // assert
        Assert.assertThat(result, IsNot.not(sameInstance(account)))
        Assert.assertThat(result.balance.amount, IsEqual.equalTo(300.toBigDecimal()))
    }

    @Test
    fun `Debit should add Debited event`() {
        // arrange
        val account = createFakeAccount()
        val money = Money(100.toBigDecimal(), Currency.getInstance("EUR"))

        // act
        val result = account.debit(money)

        // assert
        Assert.assertThat(result, IsNot.not(sameInstance(account)))
        Assert.assertThat(result.balance.amount, equalTo(300.toBigDecimal()))
        Assert.assertThat(result.domainEvents, hasItem(instanceOf(MoneyDebited::class.java)))
    }

    @Test
    fun `Credit should add Credited event`() {
        // arrange
        val account = createFakeAccount()
        val money = Money(100.toBigDecimal(), Currency.getInstance("EUR"))

        // act
        val result = account.credit(money)

        // assert
        Assert.assertThat(result, IsNot.not(sameInstance(account)))
        Assert.assertThat(result.balance.amount, equalTo(100.toBigDecimal()))
        Assert.assertThat(result.domainEvents, hasItem(instanceOf(MoneyCredited::class.java)))
    }

    private fun createFakeAccount() =
        BankAccount(UUID.randomUUID(), Money(200.toBigDecimal(), Currency.getInstance("EUR")))
}