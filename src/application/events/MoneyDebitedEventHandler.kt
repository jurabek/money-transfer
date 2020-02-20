package application.events

import domain.account.AccountTransaction
import domain.account.TransactionRepository
import domain.account.TransactionType
import domain.events.MoneyDebited
import mu.KotlinLogging
import java.util.*

class MoneyDebitedEventHandler(
    private val transactionRepository: TransactionRepository
) : EventHandler<MoneyDebited> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handle(event: MoneyDebited) {
        val debitTransaction = AccountTransaction(
            UUID.randomUUID(),
            event.accountId,
            event.money.amount,
            event.money.currency,
            TransactionType.DEBIT
        )
        transactionRepository.create(debitTransaction)
        logger.trace { "Money debit has been successfully created for account: ${event.accountId}" }
    }
}
