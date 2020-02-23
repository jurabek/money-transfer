package domain.transaction

import domain.Entity
import java.math.BigDecimal
import java.util.*

data class TransactionInfo(
    val id: UUID,
    val accountId: UUID,
    val amount: BigDecimal,
    val currency: Currency,
    val type: TransactionInfoType
) : Entity()

enum class TransactionInfoType(val value: Int) {
    CREDIT(1),
    DEBIT(2)
}
