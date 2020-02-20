package domain.events

import domain.DomainEvent
import domain.account.Money
import java.util.*

data class MoneyDebited(
    val money: Money,
    val accountId: UUID
) : DomainEvent