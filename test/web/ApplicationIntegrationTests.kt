package com.jurabek.web

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.junit.Before
import web.module
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationIntegeationTests {

    @Before
    fun init() {

    }
    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            with(handleRequest(HttpMethod.Post, "/api/v1/transfer") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(createTransfer)
            }) {
                assertEquals(HttpStatusCode.Created, response.status())
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
    }
}
