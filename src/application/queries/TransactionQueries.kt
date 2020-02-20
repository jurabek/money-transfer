package application.queries

import application.dtos.account.mapFromAccountTransaction
import domain.account.TransactionRepository
import java.util.*

class TransactionQueries(private val transactionRepository: TransactionRepository) {
    fun getTransactionsByAccountId(accountId: UUID) =
        transactionRepository.getAll()
            .filter { it.accountId == accountId }
            .map { mapFromAccountTransaction(it) }
}