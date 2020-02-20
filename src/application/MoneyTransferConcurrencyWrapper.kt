package application

import kotlinx.coroutines.sync.Mutex

interface MoneyTransferConcurrencyWrapper {
    suspend fun lock(owner: Any?)
    suspend fun unlock(owner: Any?)
}

class MoneyTransferConcurrencyWrapperIml : MoneyTransferConcurrencyWrapper {
    private val mutex = Mutex()
    override suspend fun lock(owner: Any?) = mutex.lock(owner)
    override suspend fun unlock(owner: Any?) = mutex.unlock(owner)
}