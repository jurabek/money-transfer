package domain

interface AggregateRoot

interface ValueObject

interface DomainEvent

interface Command

abstract class Entity {
    val domainEvents: MutableList<DomainEvent> = mutableListOf()
    fun addDomainEvent(event: DomainEvent) = domainEvents.add(event)
    fun clearDomainEvents() = domainEvents.clear()
}