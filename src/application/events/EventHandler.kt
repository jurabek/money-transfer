package application.events

import domain.DomainEvent

interface EventHandler<T : DomainEvent> {
    suspend fun handle(event: T)
}