package web.routers

import application.ValidationException
import application.queries.AccountQueries
import application.toUUID
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import org.koin.ktor.ext.inject

fun Route.accounts() {
    val accountQueries by inject<AccountQueries>()
    route("api/v1/accounts") {
        get("/{id}/balance") {
            val id = call.parameters["id"]?.toUUID() ?: throw ValidationException("Account ID must be provided")
            call.respond(accountQueries.getAccountBalanceById(id))
        }
        get("/all") {
            call.respond(accountQueries.getAllAccounts())
        }
    }
}