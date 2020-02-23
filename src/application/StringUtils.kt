package application

import java.util.*

fun String.toCurrency(): Currency {
    return try {
        Currency.getInstance(this)
    } catch (ex: Throwable) {
        throw ValidationException("Invalid Currency!")
    }
}

fun String.toUUID(): UUID {
    return try {
        UUID.fromString(this)
    } catch (ex: Throwable) {
        throw ValidationException("Invalid UUID!")
    }
}
