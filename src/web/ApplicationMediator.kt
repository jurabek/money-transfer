package web

import application.commands.CommandHandler
import application.events.EventHandler
import domain.Command
import domain.DomainEvent
import domain.Mediator
import mu.KotlinLogging
import org.koin.core.context.GlobalContext
import org.koin.core.qualifier.named

class ApplicationMediator : Mediator {
    private val koin = GlobalContext.get().koin
    private val logger = KotlinLogging.logger {}

    override suspend fun <TEvent : DomainEvent> publish(event: TEvent) {
        val eventHandler = koin.get<EventHandler<TEvent>>(named(event.javaClass.name))
        logger.info { "Handling event: ${event.javaClass.simpleName} ($event)" }
        eventHandler.handle(event)
    }

    override suspend fun <TCommand : Command, TResult> send(command: TCommand): TResult {
        val commandHandler = koin.get<CommandHandler<TCommand, TResult>>(
            named(command.javaClass.name)
        )
        logger.info { "Handling command: ${command.javaClass.simpleName} ($command)" }
        val result = commandHandler.handle(command)
        logger.info { "Command  ${command.javaClass.simpleName} handled - response: $result" }
        return result
    }
}
