package application

class ValidationException(override val message: String) : IllegalStateException(message)