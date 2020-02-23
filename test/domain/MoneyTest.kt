package domain

import domain.account.Money
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class MoneyTest {
    @Test(expected = DomainException::class)
    fun `Money invalid amount when Add negative or zero`() {
        // arrange
        val money = Money(BigDecimal(22.0), Currency.getInstance("EUR"))

        // assert
        money.add(Money(BigDecimal.ZERO, Currency.getInstance("EUR")))
    }

    @Test(expected = DomainException::class)
    fun `Currency invalid when Add different currency`() {
        // arrange
        val money = Money(BigDecimal(22.0), Currency.getInstance("EUR"))

        // assert
        money.add(Money(10.toBigDecimal(), Currency.getInstance("USD")))
    }

    @Test(expected = DomainException::class)
    fun `Currency invalid when Subtract different currency`() {
        // arrange
        val money = Money(BigDecimal(22.0), Currency.getInstance("EUR"))

        // assert
        money.subtract(Money(10.toBigDecimal(), Currency.getInstance("USD")))
    }

    @Test(expected = DomainException::class)
    fun `Money invalid amount when Subtract negative or zero`() {
        // arrange
        val money = Money(BigDecimal(10.0), Currency.getInstance("USD"))

        // assert
        money.subtract(Money(BigDecimal.ZERO, Currency.getInstance("USD")))
    }

    @Test(expected = DomainException::class)
    fun `Money invalid amount when actual amount less than subtracting amount`() {
        // arrange
        val money = Money(20.toBigDecimal(), Currency.getInstance("EUR"))

        // assert
        money.subtract(Money(100.toBigDecimal(), Currency.getInstance("EUR")))
    }

    @Test
    fun `Two Money instances with same values should be equal`() {
        // arrange
        val money1 = Money(10.toBigDecimal(), Currency.getInstance("EUR"))
        val money2 = Money(10.toBigDecimal(), Currency.getInstance("EUR"))

        // assert
        Assert.assertThat(money1, IsEqual.equalTo(money2))
    }

    @Test
    fun `Amount should be added`() {
        // arrange
        val money = Money(20.toBigDecimal(), Currency.getInstance("EUR"))
        val result = money.add(Money(100.toBigDecimal(), Currency.getInstance("EUR")))

        // assert
        Assert.assertThat(result.amount, IsEqual.equalTo(120.toBigDecimal()))
    }

    @Test
    fun `Amount should be subtracted`() {
        // arrange
        val money = Money(100.toBigDecimal(), Currency.getInstance("EUR"))
        val result = money.subtract(Money(50.toBigDecimal(), Currency.getInstance("EUR")))

        // assert
        Assert.assertThat(result.amount, IsEqual.equalTo(50.toBigDecimal()))
    }
}