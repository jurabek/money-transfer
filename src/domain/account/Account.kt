package domain.account

import domain.AggregateRoot
import domain.DomainException
import domain.Entity
import domain.events.MoneyDebited
import domain.events.MoneyCredited
import java.math.BigDecimal
import java.util.*

data class Account(
    val id: UUID,
    val balance: BigDecimal,
    val currency: Currency
) : Entity(), AggregateRoot {

    val availableBalance: Money get() = Money(balance, currency)

    fun credit(money: Money): Account {
        if (availableBalance.amount <= money.amount)
            throw DomainException("The available balance less than crediting amount!")

        return Account(id, availableBalance.subtract(money).amount, money.currency).apply {
            addDomainEvent(MoneyCredited(money, id))
        }
    }

    fun debit(money: Money): Account {
        return Account(id, availableBalance.add(money).amount, money.currency).apply {
            addDomainEvent(MoneyDebited(money, id))
        }
    }
}