package web.routers

import application.commands.CreateTransferCommand
import application.dtos.transfer.TransferRequestDto
import application.dtos.transfer.fromMoneyTransfer
import application.dtos.transfer.toCreateTransferCommand
import domain.Mediator
import domain.transfer.MoneyTransfer
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject

fun Route.transfer() {
    val mediator by inject<Mediator>()
    route("api/v1/transfer") {
        post {
            val dto = call.receive<TransferRequestDto>()
            val createTransferCommand = dto.toCreateTransferCommand()
            val moneyTransfer = mediator.send<CreateTransferCommand, MoneyTransfer>(createTransferCommand)
            call.respond(HttpStatusCode.Created, fromMoneyTransfer(moneyTransfer))
        }
    }
}