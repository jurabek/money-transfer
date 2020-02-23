package application.commands

import application.MutexWrapper
import application.ValidationException
import application.toCurrency
import domain.TransactionStatus
import domain.account.BankAccountRepository
import domain.transfer.MoneyTransfer
import domain.transfer.MoneyTransferRepository
import mu.KotlinLogging
import java.util.*

class CreateTransferCommandHandler(
    private val bankAccountRepository: BankAccountRepository,
    private val transferRepository: MoneyTransferRepository,
    private val mutexWrapper: MutexWrapper
) : CommandHandler<CreateTransferCommand, MoneyTransfer> {

    private val logger = KotlinLogging.logger {}

    override suspend fun handle(command: CreateTransferCommand): MoneyTransfer {
        if (command.sourceAccountId == command.targetAccountId) {
            throw ValidationException("You cannot transfer money between same accounts!")
        }

        try {
            mutexWrapper.lock()

            val sourceAccount = bankAccountRepository.getById(command.sourceAccountId)
            val targetAccount = bankAccountRepository.getById(command.targetAccountId)

            val creditedAccount = sourceAccount.credit(command.toMoney())
            bankAccountRepository.update(creditedAccount)

            val debitedAccount = targetAccount.debit(command.toMoney())
            bankAccountRepository.update(debitedAccount)

            val moneyTransfer = MoneyTransfer(
                UUID.randomUUID(), targetAccount.id, sourceAccount.id, command.amount,
                command.currency.toCurrency(), TransactionStatus.COMPLETED, command.reference
            )

            return transferRepository.create(moneyTransfer)
        } finally {
            mutexWrapper.unlock()
        }
    }
}
