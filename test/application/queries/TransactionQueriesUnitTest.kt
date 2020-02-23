package com.jurabek.application.queries

import application.queries.TransactionQueries
import application.toCurrency
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import domain.transaction.TransactionInfo
import domain.transaction.TransactionInfoRepository
import domain.transaction.TransactionInfoType
import org.junit.Test
import java.util.*

class TransactionQueriesUnitTests {
    @Test
    fun `when passed account id should return all account transactions`() {
        // arrange
        val fakeAccountId = UUID.randomUUID()
        val transaction1 = TransactionInfo(
            UUID.randomUUID(),
            fakeAccountId,
            100.toBigDecimal(),
            "EUR".toCurrency(),
            TransactionInfoType.CREDIT
        )
        val transaction2 = TransactionInfo(
            UUID.randomUUID(),
            fakeAccountId,
            50.toBigDecimal(),
            "EUR".toCurrency(),
            TransactionInfoType.DEBIT
        )

        val transactionInfoRepository = mock<TransactionInfoRepository> {
            on { getAll() } doReturn listOf(transaction1, transaction2)
        }
        val transactionQueries = TransactionQueries(transactionInfoRepository)

        // act
        val result = transactionQueries.getTransactionsByAccountId(fakeAccountId)
        
        // assert
    }
}