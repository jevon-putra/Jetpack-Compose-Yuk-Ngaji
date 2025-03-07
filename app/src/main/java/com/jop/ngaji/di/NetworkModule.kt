package com.jop.ngaji.di

import com.jop.ngaji.network.ConstantNetworkValue
import com.jop.ngaji.network.api.SurahAPI
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
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient(OkHttp)  {
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
    }

    factory { SurahAPI(get()) }
}