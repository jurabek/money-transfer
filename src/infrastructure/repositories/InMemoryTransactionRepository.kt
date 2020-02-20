package infrastructure.repositories

import domain.Mediator
import domain.account.AccountTransaction
import domain.account.TransactionRepository
import infrastructure.NotFoundException
import java.util.*

class InMemoryTransactionRepository(
    private val mediator: Mediator
) : TransactionRepository {

    private val transactions: MutableMap<UUID, AccountTransaction> = Collections.synchronizedMap(mutableMapOf())

    override fun getById(id: UUID): AccountTransaction =
        transactions[id] ?: throw NotFoundException("Transaction not found!")

    override fun getAll(): Collection<AccountTransaction> = transactions.values

    override suspend fun create(accountTransaction: AccountTransaction): AccountTransaction {
        accountTransaction.domainEvents.forEach { mediator.publish(it) }
        accountTransaction.clearDomainEvents()

        transactions[accountTransaction.id] = accountTransaction
        return getById(accountTransaction.id)
    }

    override suspend fun update(accountTransaction: AccountTransaction): AccountTransaction {
        accountTransaction.domainEvents.forEach { mediator.publish(it) }
        accountTransaction.clearDomainEvents()

        return transactions.replace(accountTransaction.id, accountTransaction)
            ?: throw NotFoundException("Update transaction failed!")
    }
}