package infrastructure.data

import domain.account.Account
import domain.account.AccountRepository
import mu.KotlinLogging
import java.math.BigDecimal
import java.util.*

suspend inline fun AccountRepository.createFakeAccounts() {
    val logger = KotlinLogging.logger { }

    val account1 = Account(UUID.fromString("591f6eb1-de7e-4d25-8afa-bd82d85ee565"), 2000.toBigDecimal(), Currency.getInstance("EUR"))
    val account2 = Account(UUID.fromString("8657792e-8329-4723-bf04-837d3649e9af"), 100.toBigDecimal(), Currency.getInstance("EUR"))
    val account3 = Account(UUID.fromString("69ee930d-8a4c-446b-a61e-a12c179bda91"), 10.toBigDecimal(), Currency.getInstance("USD"))
    val account4 = Account(UUID.fromString("d0e678a9-8eca-4d30-a02d-e497d0699b21"), BigDecimal.ZERO, Currency.getInstance("EUR"))

    this.create(account1)
    logger.info { "Account1 ID: ${account1.id}" }
    this.create(account2)
    logger.info { "Account2 ID: ${account2.id}" }
    this.create(account3)
    logger.info { "Account3 ID: ${account3.id}" }
    this.create(account4)
    logger.info { "Account4 ID: ${account4.id}" }
}