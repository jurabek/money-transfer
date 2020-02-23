package domain.events

import domain.DomainEvent
import domain.account.Money
import java.util.*

data class MoneyCreditRecorded(
    val money: Money,
    val accountId: UUID,
    val transferId: UUID
) : DomainEvent