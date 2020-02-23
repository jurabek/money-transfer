package com.jurabek.application.events

import application.events.MoneyDebitedEventHandler
import application.toCurrency
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import domain.account.Money
import domain.transaction.TransactionInfoRepository
import domain.events.MoneyDebitRecorded
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*

class MoneyDebitRecordedEventHandlerUnitTest {
    @Test
    fun `When handler is called debited transaction should be created`() {
        runBlocking {
            // arrange
            val moneyDebited = MoneyDebitRecorded(
                Money(100.toBigDecimal(), "EUR".toCurrency()),
                UUID.randomUUID(),
                UUID.randomUUID()
            )
            val transactionRepository = mock<TransactionInfoRepository>()

            // act
            val debitedEventHandler = MoneyDebitedEventHandler(transactionRepository)
            debitedEventHandler.handle(moneyDebited)

            // assert
            verify(transactionRepository, times(1)).create(any())
        }
    }
}