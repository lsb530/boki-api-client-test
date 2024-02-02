package com.boki.bokiapiclienttest

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*
import java.util.function.Consumer

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BokiApiClientTestApplicationTests {
    @Autowired
    lateinit var webTestClient: WebTestClient

    final val API_PATH = "/api"

    @DisplayName("/api")
    @Order(1)
    @Test
    fun api() {
        webTestClient.get()
            .uri(API_PATH)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
    }

    @DisplayName("/random")
    @Order(2)
    @Test
    fun random() {
        val randomId = UUID.randomUUID().toString()
        val randomEmail = "test@example.com"
        val timestamp = System.currentTimeMillis()

        webTestClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("$API_PATH/random")
                    .queryParam("id", randomId)
                    .queryParam("email", randomEmail)
                    .queryParam("ts", timestamp)
                    .build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .consumeWith { println(it) }
            .jsonPath("$.id").exists()
            .jsonPath("$.email").isEqualTo(randomEmail)
            .jsonPath("$.ts").isNumber
    }



}
