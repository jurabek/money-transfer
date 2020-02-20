package web.module

import application.MoneyTransferConcurrencyWrapper
import application.MoneyTransferConcurrencyWrapperIml
import application.commands.CommandHandler
import application.commands.CreateTransferCommand
import application.commands.CreateTransferCommandHandler
import application.events.EventHandler
import application.events.MoneyDebitedEventHandler
import application.events.MoneyCreditedEventHandler
import application.queries.AccountQueries
import application.queries.TransactionQueries
import domain.Mediator
import domain.events.MoneyDebited
import domain.events.MoneyCredited
import domain.transfer.MoneyTransfer
import web.ApplicationMediator
import org.koin.core.qualifier.named
import org.koin.dsl.module

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
    factory { TransactionQueries(get()) }
    single<Mediator> { ApplicationMediator() }
    single<MoneyTransferConcurrencyWrapper> { MoneyTransferConcurrencyWrapperIml() }
}