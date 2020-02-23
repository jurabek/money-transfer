package web.module

import application.MutexWrapper
import application.MutexWrapperIml
import application.commands.CommandHandler
import application.commands.CreateTransferCommand
import application.commands.CreateTransferCommandHandler
import application.events.EventHandler
import application.events.MoneyCreditedEventHandler
import application.events.MoneyDebitedEventHandler
import application.queries.AccountQueries
import application.queries.TransactionQueries
import domain.Mediator
import domain.events.MoneyCredited
import domain.events.MoneyDebited
import domain.transfer.MoneyTransfer
import org.koin.core.qualifier.named
import org.koin.dsl.module
import web.ApplicationMediator

val applicationModule = module {
    factory<EventHandler<MoneyDebited>>(named(MoneyDebited::class.java.name)) { MoneyDebitedEventHandler(get()) }
    factory<EventHandler<MoneyCredited>>(named(MoneyCredited::class.java.name)) { MoneyCreditedEventHandler(get()) }
    factory<CommandHandler<CreateTransferCommand, MoneyTransfer>>(named(CreateTransferCommand::class.java.name)) {
        CreateTransferCommandHandler(
            get(),
            get(),
            get()
        )
    }
    factory { AccountQueries(get()) }
    factory { TransactionQueries(get(), get()) }
    single<Mediator> { ApplicationMediator() }
    single<MutexWrapper> { MutexWrapperIml() }
}