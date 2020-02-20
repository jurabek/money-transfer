package application.dtos.account

import domain.account.AccountTransaction
import domain.account.TransactionType
import java.math.BigDecimal
import java.util.*

data class AccountTransactionDto(
    val id: UUID,
    val accountId: UUID,
    val amount: BigDecimal,
    val currency: Currency
)

fun mapFromAccountTransaction(transaction: AccountTransaction): AccountTransactionDto {
    return AccountTransactionDto(
        transaction.id,
        transaction.accountId,
        if (transaction.transactionType == TransactionType.CREDIT) transaction.amount.negate() else transaction.amount,
        transaction.currency
    )
}