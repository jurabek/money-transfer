package infrastructure.data

import application.toCurrency
import domain.account.BankAccount
import domain.account.BankAccountRepository
import domain.account.Money
import mu.KotlinLogging
import java.math.BigDecimal
import java.util.*

suspend inline fun BankAccountRepository.createFakeAccounts() {
    val logger = KotlinLogging.logger { }

    val account1 = BankAccount(
        UUID.fromString("591f6eb1-de7e-4d25-8afa-bd82d85ee565"),
        Money(2000.toBigDecimal(), "EUR".toCurrency())
    )
    val account2 = BankAccount(
        UUID.fromString("8657792e-8329-4723-bf04-837d3649e9af"),
        Money(100.toBigDecimal(), "EUR".toCurrency())
    )
    val account3 = BankAccount(
        UUID.fromString("69ee930d-8a4c-446b-a61e-a12c179bda91"),
        Money(10.toBigDecimal(), "USD".toCurrency())
    )
    val account4 = BankAccount(
        UUID.fromString("d0e678a9-8eca-4d30-a02d-e497d0699b21"),
        Money(BigDecimal.ZERO, "EUR".toCurrency())
    )

    this.create(account1)
    logger.info { "Account1 ID: ${account1.id}" }
    this.create(account2)
    logger.info { "Account2 ID: ${account2.id}" }
    this.create(account3)
    logger.info { "Account3 ID: ${account3.id}" }
    this.create(account4)
    logger.info { "Account4 ID: ${account4.id}" }
}