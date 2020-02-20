package application.commands

import application.MoneyTransferConcurrencyWrapper
import application.toCurrency
import domain.TransactionStatus
import domain.account.AccountRepository
import domain.transfer.MoneyTransfer
import domain.transfer.MoneyTransferRepository
import kotlinx.coroutines.delay
import mu.KotlinLogging
import java.util.*

class CreateTransferCommandHandler(
    private val accountRepository: AccountRepository,
    private val transferRepository: MoneyTransferRepository,
    private val moneyTransferConcurrencyWrapper: MoneyTransferConcurrencyWrapper
) : CommandHandler<CreateTransferCommand, MoneyTransfer> {

    private val logger = KotlinLogging.logger {}

    override suspend fun handle(command: CreateTransferCommand): MoneyTransfer {
        val lock = command.sourceAccountId to command.targetAccountId
        try {
            moneyTransferConcurrencyWrapper.lock(lock)
            logger.trace { "Locked by: ${command.sourceAccountId} to ${command.targetAccountId}" }

            val sourceAccount = accountRepository.getById(command.sourceAccountId)
            val targetAccount = accountRepository.getById(command.targetAccountId)

            val withdraw = sourceAccount.credit(command.toMoney())
            accountRepository.update(withdraw)

            val deposit = targetAccount.debit(command.toMoney())
            accountRepository.update(deposit)

            val moneyTransfer = MoneyTransfer(
                UUID.randomUUID(), targetAccount.id, sourceAccount.id, command.amount,
                command.currency.toCurrency(), TransactionStatus.COMPLETED, command.reference
            )

            return transferRepository.create(moneyTransfer)
        } finally {
            moneyTransferConcurrencyWrapper.unlock(lock)
            logger.trace { "Unlocked by: ${command.sourceAccountId} to ${command.targetAccountId}" }
        }
    }
}
