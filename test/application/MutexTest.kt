package application

import com.nhaarman.mockitokotlin2.mock
import domain.Mediator
import infrastructure.data.createFakeAccounts
import infrastructure.repositories.InMemoryBankAccountRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MutexTest {
    @Test
    fun testAccountOrder() {
        runBlocking {
            val mediator = mock<Mediator>()
            val accountRepository = InMemoryBankAccountRepository(mediator)
            accountRepository.createFakeAccounts()

            val accounts = accountRepository.getAll()
            val sorted = accounts.sortedBy { it.id }

            val a = 1
        }
    }
}