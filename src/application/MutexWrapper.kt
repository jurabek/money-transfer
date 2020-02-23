package application

import kotlinx.coroutines.sync.Mutex

interface MutexWrapper {
    suspend fun lock(key: Any? = null)
    fun unlock(key: Any? = null)
}

class MutexWrapperIml : MutexWrapper {
    private val mutex = Mutex()

    override suspend fun lock(key: Any?) {
        mutex.lock(key)
    }

    override fun unlock(key: Any?) {
        mutex.unlock(key)
    }
}