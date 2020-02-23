package application.events

import application.toCurrency
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import domain.account.Money
import domain.events.MoneyDebited
import domain.transaction.TransactionInfoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*

class MoneyDebitedEventHandlerUnitTest {
    @Test
    fun `When handler is called debited transaction should be created`() {
        runBlocking {
            // arrange
            val moneyDebited = MoneyDebited(
                Money(100.toBigDecimal(), "EUR".toCurrency()),
                UUID.randomUUID(),
                UUID.randomUUID()
            )
            val transactionRepository = mock<TransactionInfoRepository>()

            // act
            val debitedEventHandler = MoneyDebitedEventHandler(transactionRepository)
            debitedEventHandler.handle(moneyDebited)

            // assert
            verify(transactionRepository, times(1)).add(any())
        }
    }
}