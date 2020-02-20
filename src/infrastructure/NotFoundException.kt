package infrastructure

class NotFoundException(override val message: String) : RuntimeException(message)