package application.commands

import application.MutexWrapper
import application.ValidationException
import application.toCurrency
import com.nhaarman.mockitokotlin2.*
import domain.TransactionStatus
import domain.account.BankAccount
import domain.account.BankAccountRepository
import domain.account.Money
import domain.transfer.MoneyTransfer
import domain.transfer.MoneyTransferRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Test
import java.util.*

class MoneyTransferCommandHandlerUnitTest {

    @Test(expected = ValidationException::class)
    fun `When same account sending money should throw Validation Exception`() {
        runBlocking {
            val account1 = UUID.randomUUID()
            val transferRepository = mock<MoneyTransferRepository>()
            val accountRepository = mock<BankAccountRepository>()
            val moneyTransferConcurrencyWrapper = mock<MutexWrapper>()

            val command = CreateTransferCommand(
                account1, account1, 100.toBigDecimal(),
                "EUR", "Test transfer"
            )

            val handler = CreateTransferCommandHandler(
                accountRepository,
                transferRepository,
                moneyTransferConcurrencyWrapper
            )

            handler.handle(command)
        }
    }

    @Test
    fun `handle should should create transfer when valid params`() {
        runBlocking {
            // arrange
            val transferRepository = mock<MoneyTransferRepository>()
            val accountRepository = mock<BankAccountRepository>()
            val moneyTransferConcurrencyWrapper = mock<MutexWrapper>()

            val account1 = BankAccount(
                UUID.randomUUID(),
                Money(200.toBigDecimal(), "EUR".toCurrency())
            )

            val account2 = BankAccount(
                UUID.randomUUID(),
                Money(0.toBigDecimal(), "EUR".toCurrency())
            )

            val expectedMoneyTransfer = MoneyTransfer(
                UUID.randomUUID(), account2.id,
                account1.id, 100.toBigDecimal(),
                "EUR".toCurrency(), TransactionStatus.COMPLETED, "Test transfer"
            )

            whenever(accountRepository.getById(account1.id)).thenReturn(account1)
            whenever(accountRepository.getById(account2.id)).thenReturn(account2)
            whenever(transferRepository.add(any())).thenReturn(expectedMoneyTransfer)

            val command = CreateTransferCommand(
                account1.id, account2.id, 100.toBigDecimal(),
                "EUR", "Test transfer"
            )

            // act
            val handler = CreateTransferCommandHandler(
                accountRepository,
                transferRepository,
                moneyTransferConcurrencyWrapper
            )

            val result = handler.handle(command)

            // assert
            Assert.assertThat(result.creditAccountId, IsEqual.equalTo(account1.id))
            Assert.assertThat(result.debitAccountId, IsEqual.equalTo(account2.id))
            Assert.assertThat(result.amount, IsEqual.equalTo(100.toBigDecimal()))
            Assert.assertThat(result.currency, IsEqual.equalTo("EUR".toCurrency()))
            Assert.assertThat(result.status, IsEqual.equalTo(TransactionStatus.COMPLETED))
            verify(accountRepository, times(2)).update(any())
        }
    }
}