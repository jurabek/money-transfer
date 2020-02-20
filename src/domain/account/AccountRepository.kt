package domain.account

import java.util.*

interface AccountRepository {
    fun getById(id: UUID): Account
    fun getAll(): Collection<Account>
    suspend fun create(account: Account): Account
    suspend fun update(account: Account): Account
}