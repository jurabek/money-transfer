package application.dtos.transfer

import domain.TransactionStatus
import domain.transfer.MoneyTransfer
import java.time.format.DateTimeFormatter
import java.util.*

data class MoneyTransferResponseDto(
    val id: UUID,
    val status: TransactionStatus,
    val createdAt: String
)

fun fromMoneyTransfer(moneyTransfer: MoneyTransfer): MoneyTransferResponseDto {
    return MoneyTransferResponseDto(
        moneyTransfer.id,
        moneyTransfer.status,
        moneyTransfer.createdAt.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
    )
}
