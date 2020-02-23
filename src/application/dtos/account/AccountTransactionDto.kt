package application.dtos.account

import com.fasterxml.jackson.annotation.JsonInclude
import domain.transaction.TransactionInfo
import domain.transaction.TransactionType
import java.math.BigDecimal
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccountTransactionDto(
    val id: UUID,
    val accountId: UUID,
    val amount: BigDecimal,
    val currency: String,
    val type: String = "transfer",
    var from: UUID? = null,
    var to: UUID? = null
)

fun mapFromAccountTransaction(transactionInfo: TransactionInfo): AccountTransactionDto {
    return AccountTransactionDto(
        transactionInfo.id,
        transactionInfo.accountId,
        if (transactionInfo.type == TransactionType.DEBIT) {
            transactionInfo.amount.negate()
        } else {
            transactionInfo.amount
        },
        transactionInfo.currency.toString()
    )
}