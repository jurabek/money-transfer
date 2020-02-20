package domain.transfer

import domain.Entity
import domain.TransactionStatus
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

data class MoneyTransfer(
    val id: UUID,
    val debitAccountId: UUID,
    val creditAccountId: UUID,
    val amount: BigDecimal,
    val currency: Currency,
    val status: TransactionStatus = TransactionStatus.PENDING,
    val reference: String? = null,
    val createdAt: ZonedDateTime = ZonedDateTime.now()
) : Entity()