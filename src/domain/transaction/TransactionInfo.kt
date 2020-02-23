package domain.transaction

import domain.Entity
import java.math.BigDecimal
import java.util.*

data class TransactionInfo(
    val id: UUID,
    val accountId: UUID,
    val transferId: UUID,
    val amount: BigDecimal,
    val currency: Currency,
    val type: TransactionType
) : Entity()

enum class TransactionType(val value: Int) {
    CREDIT(1),
    DEBIT(2)
}
