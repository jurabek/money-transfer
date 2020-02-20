package application.dtos.account

import java.math.BigDecimal
import java.util.*

data class AccountBalanceDto(
    val amount: BigDecimal,
    val currency: Currency
)