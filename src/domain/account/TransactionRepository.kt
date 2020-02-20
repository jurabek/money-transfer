package domain.account

import java.util.*

interface TransactionRepository {
    fun getById(id: UUID): AccountTransaction
    fun getAll(): Collection<AccountTransaction>
    suspend fun create(accountTransaction: AccountTransaction): AccountTransaction
    suspend fun update(accountTransaction: AccountTransaction): AccountTransaction
}