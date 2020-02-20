package web.routers

import application.queries.AccountQueries
import application.toUUID
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import org.koin.ktor.ext.inject

fun Route.accounts() {
    val accountQueries by inject<AccountQueries>()
    route("/accounts") {
        get("/{id}/balance") {
            val id = call.parameters["id"]?.toUUID() ?: throw IllegalStateException("Account ID must be provided")
            call.respond(accountQueries.getAccountBalanceById(id))
        }
        get("/getAll") {
            call.respond(accountQueries.getAllAccounts())
        }
    }

    get("/") {
        call.respondText("HELLO WORLD!", contentType = ContentType.Application.Json)
    }

    get("/json/jackson") {
        call.respond(mapOf("hello" to "world"))
    }
}