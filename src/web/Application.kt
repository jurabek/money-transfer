package web

import application.ValidationException
import com.fasterxml.jackson.databind.SerializationFeature
import domain.DomainException
import domain.account.BankAccountRepository
import infrastructure.data.createFakeAccounts
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KLogger
import mu.KotlinLogging
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import web.module.applicationModule
import web.module.infrastructureModule
import web.routers.accounts
import web.routers.transactions
import web.routers.transfer

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, commandLineEnvironment(args))
    server.start(wait = true)
}

fun Application.module(testing: Boolean = false) {
    val logger = KotlinLogging.logger {}
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(Koin) {
        modules(listOf(applicationModule, infrastructureModule))
    }
    install(Routing) {
        accounts()
        transfer()
        transactions()
    }
    install(StatusPages) {
        exception<DomainException> { ex ->
            logAndRespondBadRequest(logger, ex)
        }
        exception<Throwable> { ex ->
            logAndRespondBadRequest(logger, ex)
        }
        exception<ValidationException> { ex ->
            logAndRespondBadRequest(logger, ex)
        }
    }

    GlobalScope.launch {
        val accountRepository by inject<BankAccountRepository>()
        accountRepository.createFakeAccounts()
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.logAndRespondBadRequest(
    logger: KLogger,
    ex: Throwable
) {
    logger.error { ex.message }
    call.respond(HttpStatusCode.BadRequest, mapOf("error" to ex.message))
}
