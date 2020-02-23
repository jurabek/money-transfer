package application.queries

import application.dtos.account.mapFromAccountTransaction
import domain.transaction.TransactionInfoRepository
import domain.transaction.TransactionType
import domain.transfer.MoneyTransferRepository
import java.util.*

class TransactionQueries(
    private val transactionInfoRepository: TransactionInfoRepository,
    private val moneyTransferRepository: MoneyTransferRepository
) {
    fun getTransactionsByAccountId(accountId: UUID) =
        transactionInfoRepository.getAll()
            .filter { it.accountId == accountId }
            .map {
                mapFromAccountTransaction(it).apply {
                    val transfer = moneyTransferRepository.getById(it.transferId)
                    if (it.type == TransactionType.DEBIT) {
                        this.to = transfer.creditAccountId
                    } else {
                        this.from = transfer.debitAccountId
                    }
                }
            }
}