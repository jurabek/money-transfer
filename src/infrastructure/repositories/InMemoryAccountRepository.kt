package infrastructure.repositories

import domain.Mediator
import domain.account.Account
import domain.account.AccountRepository
import infrastructure.NotFoundException
import java.util.*

class InMemoryAccountRepository(
    private val mediator: Mediator
) : AccountRepository {

    private val accounts: MutableMap<UUID, Account> = Collections.synchronizedMap(mutableMapOf())

    override fun getById(id: UUID) = accounts.get(id) ?: throw NotFoundException("Account not found!")

    override fun getAll() = accounts.values

    override suspend fun create(account: Account): Account {
        // before the creating object, raise domain events if there is any
        account.domainEvents.forEach { mediator.publish(it) }
        account.clearDomainEvents()

        accounts[account.id] = account
        return getById(account.id)
    }

    override suspend fun update(account: Account): Account {
        // before the creating object, raise domain events if there is any

        account.domainEvents.forEach { mediator.publish(it) }
        account.clearDomainEvents()

        return accounts.replace(account.id, account) ?: throw NotFoundException("Account not found!")
    }
}