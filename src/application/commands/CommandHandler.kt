package application.commands

import domain.Command

interface CommandHandler<T : Command, out TResponse> {
    suspend fun handle(command: T): TResponse
}