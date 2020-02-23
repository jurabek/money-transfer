package application.events

import domain.transaction.TransactionInfo
import domain.transaction.TransactionInfoRepository
import domain.transaction.TransactionInfoType
import domain.events.MoneyCredited
import mu.KotlinLogging
import java.util.*

class MoneyCreditedEventHandler(
    private val transactionInfoRepository: TransactionInfoRepository
) : EventHandler<MoneyCredited> {

    private val logger = KotlinLogging.logger {}

    override suspend fun handle(event: MoneyCredited) {
        val creditTransaction = TransactionInfo(
            UUID.randomUUID(),
            event.accountId,
            event.money.amount,
            event.money.currency,
            TransactionInfoType.CREDIT
        )
        transactionInfoRepository.create(creditTransaction)
        logger.trace { "Money credit has been successfully created for account: ${event.accountId}" }
    }
}
