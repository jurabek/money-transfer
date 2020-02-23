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

    fun credit(credit: Credit): BankAccount {
        val money = credit.money
        return BankAccount(id, balance.add(money)).apply {
            addDomainEvent(MoneyCredited(money, id, credit.transferId))
        }
    }

    fun debit(debit: Debit): BankAccount {
        val money = debit.money
        if (balance.amount <= money.amount)
            throw DomainException("The available balance less than withdrawing amount!")

        return BankAccount(id, balance.subtract(money)).apply {
            addDomainEvent(MoneyDebited(money, id, debit.transferId))
        }
    }
}