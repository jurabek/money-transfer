package application.commands

import application.MutexWrapper
import application.ValidationException
import application.toCurrency
import domain.TransactionStatus
import domain.account.BankAccountRepository
import domain.account.Credit
import domain.account.Debit
import domain.transfer.MoneyTransfer
import domain.transfer.MoneyTransferRepository
import java.util.*

class CreateTransferCommandHandler(
    private val bankAccountRepository: BankAccountRepository,
    private val transferRepository: MoneyTransferRepository,
    private val mutexWrapper: MutexWrapper
) : CommandHandler<CreateTransferCommand, MoneyTransfer> {

    override suspend fun handle(command: CreateTransferCommand): MoneyTransfer {
        if (command.sourceAccountId == command.targetAccountId) {
            throw ValidationException("You cannot transfer money between same accounts!")
        }
        try {
            mutexWrapper.lock()
            val sourceAccount = bankAccountRepository.getById(command.sourceAccountId)
            val targetAccount = bankAccountRepository.getById(command.targetAccountId)

            val moneyTransfer = MoneyTransfer(
                UUID.randomUUID(), sourceAccount.id, targetAccount.id, command.amount,
                command.currency.toCurrency(), TransactionStatus.COMPLETED, command.reference
            )
            val debitedAccount = sourceAccount.debit(Debit(command.toMoney(), moneyTransfer.id))
            bankAccountRepository.update(debitedAccount)

            val creditedAccount = targetAccount.credit(Credit(command.toMoney(), moneyTransfer.id))
            bankAccountRepository.update(creditedAccount)

            return transferRepository.add(moneyTransfer)
        } finally {
            mutexWrapper.unlock()
        }
    }
}
