package application.dtos.account

import domain.transaction.TransactionInfo
import domain.transaction.TransactionInfoType
import java.math.BigDecimal
import java.util.*

data class AccountTransactionDto(
    val id: UUID,
    val accountId: UUID,
    val amount: BigDecimal,
    val currency: Currency
)

fun mapFromAccountTransaction(transactionInfo: TransactionInfo): AccountTransactionDto {
    return AccountTransactionDto(
        transactionInfo.id,
        transactionInfo.accountId,
        if (transactionInfo.type == TransactionInfoType.CREDIT) transactionInfo.amount.negate() else transactionInfo.amount,
        transactionInfo.currency
    )
}