package application.dtos.transfer

import application.commands.CreateTransferCommand
import java.math.BigDecimal
import java.util.*

data class TransferRequestDto(
    val sourceAccountId: UUID,
    val targetAccountId: UUID,
    val amount: BigDecimal,
    val currency: String,
    val reference: String? = null
)

fun TransferRequestDto.toCreateTransferCommand() =
    CreateTransferCommand(this.sourceAccountId, this.targetAccountId, this.amount, this.currency)
