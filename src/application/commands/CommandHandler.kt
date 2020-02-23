package application.commands

import domain.Command

interface CommandHandler<in T : Command, out TResponse> {
    suspend fun handle(command: T): TResponse
}