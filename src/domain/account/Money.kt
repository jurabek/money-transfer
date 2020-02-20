package domain.account

import domain.DomainException
import domain.ValueObject
import java.math.BigDecimal
import java.util.*

data class Money(
    val amount: BigDecimal,
    val currency: Currency
) : ValueObject {

    fun add(money: Money): Money {
        validateAmount(money.amount)
        validateCurrency(money.currency)

        return Money(amount.add(money.amount), money.currency)
    }

    fun subtract(money: Money): Money {
        validateAmount(money.amount)
        validateCurrency(money.currency)

        if (amount - money.amount < BigDecimal.ZERO)
            throw DomainException("The actual amount less than subtracting amount!")

        return Money(amount.subtract(money.amount), money.currency)
    }

    private fun validateAmount(amount: BigDecimal) {
        if (amount <= BigDecimal.ZERO)
            throw DomainException("Invalid amount!")
    }

    private fun validateCurrency(currency: Currency) {
        if (this.currency != currency)
            throw DomainException("Invalid currency!, Currency must be same")
    }
}
