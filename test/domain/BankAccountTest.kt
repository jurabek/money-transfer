package domain

import domain.account.BankAccount
import domain.account.Credit
import domain.account.Debit
import domain.account.Money
import domain.events.MoneyCredited
import domain.events.MoneyDebited
import org.hamcrest.CoreMatchers.*
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsNot
import org.junit.Assert
import org.junit.Test
import java.util.*

class BankAccountTest {

    @Test(expected = DomainException::class)
    fun `Debit should throw Domain exception when available balance less than crediting amount`() {
        // arrange
        val account = createFakeAccount()
        val transferId = UUID.randomUUID()
        val money = Money(300.toBigDecimal(), Currency.getInstance("EUR"))
        val debit = Debit(money, transferId)
        // act
        account.debit(debit)
    }

    @Test
    fun `Credit should add from actual balance and create new instance`() {
        // arrange
        val account = createFakeAccount()
        val money = Money(100.toBigDecimal(), Currency.getInstance("EUR"))
        val transferId = UUID.randomUUID()
        val credit = Credit(money, transferId)
        // act
        val result = account.credit(credit)

        // assert
        Assert.assertThat(result, IsNot.not(sameInstance(account)))
        Assert.assertThat(result.balance.amount, equalTo(300.toBigDecimal()))
    }

    @Test
    fun `Debit should subtract to actual balance and create new instance`() {
        // arrange
        val account = createFakeAccount()
        val transferId = UUID.randomUUID()
        val money = Money(100.toBigDecimal(), Currency.getInstance("EUR"))
        val debit = Debit(money, transferId)

        // act
        val result = account.debit(debit)

        // assert
        Assert.assertThat(result, IsNot.not(sameInstance(account)))
        Assert.assertThat(result.balance.amount, IsEqual.equalTo(100.toBigDecimal()))
    }

    @Test
    fun `Debit should add Debited event`() {
        // arrange
        val account = createFakeAccount()
        val transferId = UUID.randomUUID()
        val money = Money(100.toBigDecimal(), Currency.getInstance("EUR"))
        val debit = Debit(money, transferId)

        // act
        val result = account.debit(debit)

        // assert
        Assert.assertThat(result, IsNot.not(sameInstance(account)))
        Assert.assertThat(result.balance.amount, equalTo(100.toBigDecimal()))
        Assert.assertThat(result.domainEvents, hasItem(instanceOf(MoneyDebited::class.java)))
    }

    @Test
    fun `Credit should add Credited event`() {
        // arrange
        val account = createFakeAccount()
        val money = Money(100.toBigDecimal(), Currency.getInstance("EUR"))
        val transferId = UUID.randomUUID()
        val credit = Credit(money, transferId)

        // act
        val result = account.credit(credit)

        // assert
        Assert.assertThat(result, IsNot.not(sameInstance(account)))
        Assert.assertThat(result.balance.amount, equalTo(300.toBigDecimal()))
        Assert.assertThat(result.domainEvents, hasItem(instanceOf(MoneyCredited::class.java)))
    }

    private fun createFakeAccount() =
        BankAccount(UUID.randomUUID(), Money(200.toBigDecimal(), Currency.getInstance("EUR")))
}