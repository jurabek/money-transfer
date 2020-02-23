package application.queries

import application.dtos.account.AccountTransactionDto
import application.toCurrency
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import domain.TransactionStatus
import domain.transaction.TransactionInfo
import domain.transaction.TransactionInfoRepository
import domain.transaction.TransactionType
import domain.transfer.MoneyTransfer
import domain.transfer.MoneyTransferRepository
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class TransactionQueriesUnitTest {
    @Test
    fun `when passed account id should return all account transactions`() {
        // arrange
        val fakeAccountId = UUID.randomUUID()
        val fakeTransfer =
            MoneyTransfer(
                UUID.randomUUID(), fakeAccountId,
                UUID.randomUUID(),
                200.toBigDecimal(),
                "EUR".toCurrency(),
                TransactionStatus.COMPLETED
            )

        val transaction1 = TransactionInfo(
            UUID.randomUUID(),
            fakeAccountId,
            fakeTransfer.id,
            100.toBigDecimal(),
            "EUR".toCurrency(),
            TransactionType.CREDIT
        )
        val transaction2 = TransactionInfo(
            UUID.randomUUID(),
            fakeAccountId,
            fakeTransfer.id,
            50.toBigDecimal(),
            "EUR".toCurrency(),
            TransactionType.DEBIT
        )

        val transactionInfoRepository = mock<TransactionInfoRepository> {
            on { getAll() } doReturn listOf(transaction1, transaction2)
        }
        val transferRepository = mock<MoneyTransferRepository> {
            on { getById(fakeTransfer.id) } doReturn fakeTransfer
        }
        val transactionQueries = TransactionQueries(transactionInfoRepository, transferRepository)

        // act
        val result = transactionQueries.getTransactionsByAccountId(fakeAccountId)

        // assert
        assertThat(result, hasItem(instanceOf(AccountTransactionDto::class.java)))
        assertThat(result.size, equalTo(2))
        assertThat(result[0].amount, equalTo(BigDecimal(100)))
        assertThat(result[1].amount, equalTo(BigDecimal(50).negate()))
    }
}