package application.events

import domain.events.MoneyDebited
import domain.transaction.TransactionInfo
import domain.transaction.TransactionInfoRepository
import domain.transaction.TransactionType
import mu.KotlinLogging
import java.util.*

class MoneyDebitedEventHandler(
    private val transactionInfoRepository: TransactionInfoRepository
) : EventHandler<MoneyDebited> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handle(event: MoneyDebited) {
        val debitTransaction = TransactionInfo(
            UUID.randomUUID(),
            event.accountId,
            event.transferId,
            event.money.amount,
            event.money.currency,
            TransactionType.DEBIT
        )
        transactionInfoRepository.add(debitTransaction)
        logger.trace { "Money debit has been successfully created for account: ${event.accountId}" }
    }
}
