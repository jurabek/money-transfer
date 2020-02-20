package web.routers

import application.queries.TransactionQueries
import application.toUUID
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import org.koin.ktor.ext.inject

fun Route.transactions() {
    val transactionQuery by inject<TransactionQueries>()
    route("/transactions") {
        get("/account/{id}") {
            val id = call.parameters["id"]?.toUUID() ?: throw IllegalStateException("Account ID must be provided")
            val transactions = transactionQuery.getTransactionsByAccountId(id)
            call.respond(transactions)
        }
    }
}