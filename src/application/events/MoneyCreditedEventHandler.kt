package application.events

import domain.account.AccountTransaction
import domain.account.TransactionRepository
import domain.account.TransactionType
import domain.events.MoneyCredited
import mu.KotlinLogging
import java.util.*

class MoneyCreditedEventHandler(
    private val transactionRepository: TransactionRepository
) : EventHandler<MoneyCredited> {

    private val logger = KotlinLogging.logger {}

    override suspend fun handle(event: MoneyCredited) {

        val creditTransaction = AccountTransaction(
            UUID.randomUUID(),
            event.accountId,
            event.money.amount,
            event.money.currency,
            TransactionType.CREDIT
        )
        transactionRepository.create(creditTransaction)
        logger.trace { "Money credit has been successfully created for account: ${event.accountId}" }
    }
}
