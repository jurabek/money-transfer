package domain.account

import domain.AggregateRoot
import domain.DomainException
import domain.Entity
import domain.events.MoneyCredited
import domain.events.MoneyDebited
import java.util.*

data class BankAccount(
    val id: UUID,
    val balance: Money
) : Entity(), AggregateRoot {

    fun credit(money: Money): BankAccount {
        if (balance.amount <= money.amount)
            throw DomainException("The available balance less than withdrawing amount!")

        return BankAccount(id, balance.subtract(money)).apply {
            addDomainEvent(MoneyCredited(money, id))
        }
    }

    fun debit(money: Money): BankAccount {
        return BankAccount(id, balance.add(money)).apply {
            addDomainEvent(MoneyDebited(money, id))
        }
    }
}