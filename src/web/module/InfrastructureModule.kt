package web.module

import domain.account.AccountRepository
import domain.account.TransactionRepository
import domain.transfer.MoneyTransferRepository
import infrastructure.repositories.InMemoryAccountRepository
import infrastructure.repositories.InMemoryTransactionRepository
import infrastructure.repositories.InMemoryTransferRepository
import org.koin.dsl.module

val infrastructureModule = module {
    single<AccountRepository> { InMemoryAccountRepository(get()) }
    single<TransactionRepository> { InMemoryTransactionRepository(get()) }
    single<MoneyTransferRepository> { InMemoryTransferRepository() }
}