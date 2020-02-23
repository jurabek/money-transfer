package application.events

import domain.DomainEvent

interface EventHandler<in T : DomainEvent> {
    suspend fun handle(event: T)
}