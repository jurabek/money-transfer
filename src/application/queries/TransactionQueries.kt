package application.queries

import application.dtos.account.mapFromAccountTransaction
import domain.transaction.TransactionInfoRepository
import java.util.*

class TransactionQueries(private val transactionInfoRepository: TransactionInfoRepository) {
    fun getTransactionsByAccountId(accountId: UUID) =
        transactionInfoRepository.getAll()
            .filter { it.accountId == accountId }
            .map { mapFromAccountTransaction(it) }
}