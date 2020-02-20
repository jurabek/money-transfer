package domain.account

import domain.Entity
import java.math.BigDecimal
import java.util.*

data class AccountTransaction(
    val id: UUID,
    val accountId: UUID,
    val amount: BigDecimal,
    val currency: Currency,
    val transactionType: TransactionType
) : Entity()

enum class TransactionType(val value: Int) {
    CREDIT(1),
    DEBIT(2)
}
