package web

import application.dtos.account.AccountBalanceDto
import application.dtos.account.AccountTransactionDto
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationIntegrationTests {
    private val mapper = jacksonObjectMapper()
    @Test
    fun testApplication() {
        withTestApplication({ module(testing = true) }) {

            // When valid input should create transfer
            with(handleRequest(HttpMethod.Post, "/api/v1/transfer") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(createTransfer)
            }) {
                assertEquals(HttpStatusCode.Created, response.status())
            }
            with(handleRequest(HttpMethod.Get, "/api/v1/accounts/591f6eb1-de7e-4d25-8afa-bd82d85ee565/balance")) {
                assertEquals(HttpStatusCode.OK, response.status())
                val accountBalance = jacksonObjectMapper().readValue<AccountBalanceDto>(response.content!!)
                assertEquals(1900.toBigDecimal(), accountBalance.amount)
            }
            with(handleRequest(HttpMethod.Get, "/api/v1/accounts/d0e678a9-8eca-4d30-a02d-e497d0699b21/balance")) {
                assertEquals(HttpStatusCode.OK, response.status())
                val accountBalance = mapper.readValue<AccountBalanceDto>(response.content!!)
                assertEquals(100.toBigDecimal(), accountBalance.amount)
            }
            with(
                handleRequest(
                    HttpMethod.Get,
                    "/api/v1/transactions/account/591f6eb1-de7e-4d25-8afa-bd82d85ee565"
                )
            ) {
                assertEquals(HttpStatusCode.OK, response.status())
                val transaction = mapper.readValue(
                    response.content,
                    object : TypeReference<List<AccountTransactionDto>>() {})[0]

                assertEquals(100.toBigDecimal().negate(), transaction.amount)
                assertEquals(UUID.fromString("591f6eb1-de7e-4d25-8afa-bd82d85ee565"), transaction.accountId)
                assertEquals(UUID.fromString("d0e678a9-8eca-4d30-a02d-e497d0699b21"), transaction.to)
            }

            with(
                handleRequest(
                    HttpMethod.Get,
                    "/api/v1/transactions/account/d0e678a9-8eca-4d30-a02d-e497d0699b21"
                )
            ) {
                assertEquals(HttpStatusCode.OK, response.status())
                val transaction = mapper.readValue(
                    response.content,
                    object : TypeReference<List<AccountTransactionDto>>() {})[0]

                assertEquals(100.toBigDecimal(), transaction.amount)
                assertEquals(UUID.fromString("d0e678a9-8eca-4d30-a02d-e497d0699b21"), transaction.accountId)
                assertEquals(UUID.fromString("591f6eb1-de7e-4d25-8afa-bd82d85ee565"), transaction.from)
            }
        }
    }

    @Test
    fun testInvalidCases() {
        withTestApplication({ module(testing = true) }) {
            with(handleRequest(HttpMethod.Post, "/api/v1/transfer") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(invalidCurrencyTransfer)
            }) {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                val result = mapper.readValue(response.content, Map::class.java)
                assertEquals(result["error"], "Invalid currency!, Currency must be same")
            }

            with(handleRequest(HttpMethod.Post, "/api/v1/transfer") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(lessAmountToSendTransfer)
            }) {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                val result = mapper.readValue(response.content, Map::class.java)
                assertEquals(result["error"], "The available balance less than withdrawing amount!")
            }
        }
    }

    companion object {
        val createTransfer = """
            {
            	"sourceAccountId" : "591f6eb1-de7e-4d25-8afa-bd82d85ee565",
            	"targetAccountId" : "d0e678a9-8eca-4d30-a02d-e497d0699b21",
            	"amount": 100,
            	"currency": "EUR",
            	"reference": "Some message"
            }
        """.trimIndent()

        val invalidCurrencyTransfer = """
            {
            	"sourceAccountId" : "591f6eb1-de7e-4d25-8afa-bd82d85ee565",
            	"targetAccountId" : "d0e678a9-8eca-4d30-a02d-e497d0699b21",
            	"amount": 100,
            	"currency": "USD",
            	"reference": "Some message"
            }
        """.trimIndent()

        val lessAmountToSendTransfer = """
            {
            	"sourceAccountId" : "69ee930d-8a4c-446b-a61e-a12c179bda91",
            	"targetAccountId" : "d0e678a9-8eca-4d30-a02d-e497d0699b21",
            	"amount": 100,
            	"currency": "USD",
            	"reference": "Some message"
            }
        """.trimIndent()
    }
}
