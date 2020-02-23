package application.events

import domain.transaction.TransactionInfo
import domain.transaction.TransactionInfoRepository
import domain.transaction.TransactionInfoType
import domain.events.MoneyDebited
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
            event.money.amount,
            event.money.currency,
            TransactionInfoType.DEBIT
        )
        transactionInfoRepository.create(debitTransaction)
        logger.trace { "Money debit has been successfully created for account: ${event.accountId}" }
    }
}
