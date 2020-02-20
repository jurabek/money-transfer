package application.commands

import application.toCurrency
import domain.Command
import domain.account.Money
import java.math.BigDecimal
import java.util.*

data class CreateTransferCommand(
    val sourceAccountId: UUID,
    val targetAccountId: UUID,
    val amount: BigDecimal,
    val currency: String,
    val reference: String? = null
) : Command

fun CreateTransferCommand.toMoney(): Money {
    return Money(this.amount, this.currency.toCurrency())
}