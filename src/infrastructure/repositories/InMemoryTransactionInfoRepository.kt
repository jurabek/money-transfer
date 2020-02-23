package infrastructure.repositories

import domain.Mediator
import domain.transaction.TransactionInfo
import domain.transaction.TransactionInfoRepository
import infrastructure.NotFoundException
import java.util.*

class InMemoryTransactionInfoRepository(
    private val mediator: Mediator
) : TransactionInfoRepository {

    private val transactions: MutableMap<UUID, TransactionInfo> = Collections.synchronizedMap(mutableMapOf())

    override fun getById(id: UUID): TransactionInfo =
        transactions[id] ?: throw NotFoundException("Transaction not found!")

    override fun getAll(): Collection<TransactionInfo> = transactions.values

    override suspend fun add(transactionInfo: TransactionInfo): TransactionInfo {
        transactionInfo.domainEvents.forEach { mediator.publish(it) }
        transactionInfo.clearDomainEvents()

        transactions[transactionInfo.id] = transactionInfo
        return getById(transactionInfo.id)
    }

    override suspend fun update(transactionInfo: TransactionInfo): TransactionInfo {
        transactionInfo.domainEvents.forEach { mediator.publish(it) }
        transactionInfo.clearDomainEvents()

        return transactions.replace(transactionInfo.id, transactionInfo)
            ?: throw NotFoundException("Update transaction failed!")
    }
}