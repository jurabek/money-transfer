package domain

class DomainException(override val message: String) : RuntimeException(message)