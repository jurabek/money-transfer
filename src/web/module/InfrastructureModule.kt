package web.module

import domain.account.BankAccountRepository
import domain.transaction.TransactionInfoRepository
import domain.transfer.MoneyTransferRepository
import infrastructure.repositories.InMemoryBankAccountRepository
import infrastructure.repositories.InMemoryTransactionInfoRepository
import infrastructure.repositories.InMemoryTransferRepository
import org.koin.dsl.module

val infrastructureModule = module {
    single<BankAccountRepository> { InMemoryBankAccountRepository(get()) }
    single<TransactionInfoRepository> { InMemoryTransactionInfoRepository(get()) }
    single<MoneyTransferRepository> { InMemoryTransferRepository() }
}