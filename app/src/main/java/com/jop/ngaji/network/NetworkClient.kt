package com.jop.ngaji.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class NetworkClient {

    fun quranClient() = HttpClient(OkHttp) {
        defaultRequest {
            url {
                takeFrom(ConstantNetworkValue.BASE_URL_QURAN)
            }
            contentType(ContentType.Application.Json)
        }
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.BODY
        }
        install(HttpTimeout) {
            val timeout = 30000L
            connectTimeoutMillis = timeout
            requestTimeoutMillis = timeout
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }

    fun prayClient() = HttpClient(OkHttp) {
        defaultRequest {
            url {
                takeFrom(ConstantNetworkValue.BASE_URL_PRAY)
            }
            contentType(ContentType.Application.Json)
        }
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.BODY
        }
        install(HttpTimeout) {
            val timeout = 30000L
            connectTimeoutMillis = timeout
            requestTimeoutMillis = timeout
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }
}