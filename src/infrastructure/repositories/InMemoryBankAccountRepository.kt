package infrastructure.repositories

import domain.Mediator
import domain.account.BankAccount
import domain.account.BankAccountRepository
import infrastructure.NotFoundException
import java.util.*

class InMemoryBankAccountRepository(
    private val mediator: Mediator
) : BankAccountRepository {

    private val accounts: MutableMap<UUID, BankAccount> = Collections.synchronizedMap(mutableMapOf())

    override fun getById(id: UUID) = accounts.get(id) ?: throw NotFoundException("Account not found!")

    override fun getAll() = accounts.values

    override suspend fun create(bankAccount: BankAccount): BankAccount {
        // before the creating object, raise domain events if there is any
        bankAccount.domainEvents.forEach { mediator.publish(it) }
        bankAccount.clearDomainEvents()

        accounts[bankAccount.id] = bankAccount
        return getById(bankAccount.id)
    }

    override suspend fun update(bankAccount: BankAccount): BankAccount {
        // before the creating object, raise domain events if there is any

        bankAccount.domainEvents.forEach { mediator.publish(it) }
        bankAccount.clearDomainEvents()

        return accounts.replace(bankAccount.id, bankAccount) ?: throw NotFoundException("Account not found!")
    }
}