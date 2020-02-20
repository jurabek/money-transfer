package domain

interface Mediator {
    suspend fun <TEvent : DomainEvent> publish(event: TEvent)
    suspend fun <TCommand : Command, TResponse> send(command: TCommand): TResponse
}