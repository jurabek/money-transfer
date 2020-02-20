package com.jurabek.application

import application.MoneyTransferConcurrencyWrapperIml
import application.commands.CreateTransferCommand
import application.commands.CreateTransferCommandHandler
import domain.Mediator
import domain.transfer.MoneyTransferRepository
import infrastructure.data.createFakeAccounts
import infrastructure.repositories.InMemoryAccountRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.mock

class MoneyTransferCommandHandlerFunctionalTest {
    @Test
    fun `Debit and Withdraw should be synchronized when same account debits or credits in simultaneously`() {
        runBlocking {
            // arrange
            val mediator = mock(Mediator::class.java)
            val transferRepository = mock(MoneyTransferRepository::class.java)

            val moneyTransferConcurrencyFacade = MoneyTransferConcurrencyWrapperIml()
            val accountRepository = InMemoryAccountRepository(mediator)
            accountRepository.createFakeAccounts()

            val account1 = accountRepository.getAll().toList()[0]
            val account2 = accountRepository.getAll().toList()[1]
            val account3 = accountRepository.getAll().toList()[3]

            // action
            val command = CreateTransferCommand(
                account1.id, account2.id, 1.toBigDecimal(),
                "EUR", "Test transfer"
            )
            val command2 = CreateTransferCommand(
                account1.id, account3.id, 1.toBigDecimal(),
                "EUR", "Test transfer 2"
            )
            val handler =
                CreateTransferCommandHandler(accountRepository, transferRepository, moneyTransferConcurrencyFacade)

            val handler2 =
                CreateTransferCommandHandler(accountRepository, transferRepository, moneyTransferConcurrencyFacade)

            val results1 = (1..500).map {
                GlobalScope.async {
                    handler.handle(command)
                }
            }
            val results2 = (1..500).map {
                GlobalScope.async {
                    handler2.handle(command2)
                }
            }
            results1.union(results2).forEach { it.await() }

            // assert
            val withdrawAccount = accountRepository.getById(account1.id)
            val debitAccount = accountRepository.getById(account2.id)
            Assert.assertThat(1000.toBigDecimal(), IsEqual.equalTo(withdrawAccount.balance))
            Assert.assertThat(600.toBigDecimal(), IsEqual.equalTo(debitAccount.balance))
        }
    }
}