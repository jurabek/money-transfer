package application.events

import application.toCurrency
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import domain.account.Money
import domain.events.MoneyCredited
import domain.transaction.TransactionInfoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*

class MoneyCreditedEventHandlerUnitTest {

    @Test
    fun `When handler is called credit transaction should be created`() {
        runBlocking {
            // arrange
            val creditedEvent = MoneyCredited(
                Money(100.toBigDecimal(), "EUR".toCurrency()),
                UUID.randomUUID(),
                UUID.randomUUID()
            )
            val transactionRepository = mock<TransactionInfoRepository>()

            // act
            val creditedEventHandler = MoneyCreditedEventHandler(transactionRepository)
            creditedEventHandler.handle(creditedEvent)

            // assert
            verify(transactionRepository, times(1)).add(any())
        }
    }
}