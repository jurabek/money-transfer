package application

import java.util.*

fun String.toCurrency(): Currency = Currency.getInstance(this)

fun String.toUUID(): UUID = UUID.fromString(this)
