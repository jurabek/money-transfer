package domain

import domain.account.Account
import domain.account.Money
import domain.events.MoneyCredited
import domain.events.MoneyDebited
import org.hamcrest.CoreMatchers.*
import org.hamcrest.core.*
import org.junit.Assert
import org.junit.Test
import java.util.*

class AccountTest {
    @Test
    fun `Account balance must be computed as a Money instance`() {
        // arrange
        val account = createFakeAccount()

        // act
        val result = account.availableBalance

        // assert
        Assert.assertThat(result, Is.isA(Money::class.java))
        Assert.assertThat(result.amount, equalTo(account.balance))
        Assert.assertThat(result.currency, equalTo(account.currency))
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
        Assert.assertThat(result.balance, equalTo(100.toBigDecimal()))
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
        Assert.assertThat(result.balance, IsEqual.equalTo(300.toBigDecimal()))
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
        Assert.assertThat(result.balance, equalTo(300.toBigDecimal()))
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
        Assert.assertThat(result.balance, equalTo(100.toBigDecimal()))
        Assert.assertThat(result.domainEvents, hasItem(instanceOf(MoneyCredited::class.java)))
    }

    private fun createFakeAccount() =
        Account(UUID.randomUUID(), 200.toBigDecimal(), Currency.getInstance("EUR"))
}