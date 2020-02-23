package application.dtos.account

import java.math.BigDecimal

data class AccountBalanceDto(
    val amount: BigDecimal,
    val currency: String
)