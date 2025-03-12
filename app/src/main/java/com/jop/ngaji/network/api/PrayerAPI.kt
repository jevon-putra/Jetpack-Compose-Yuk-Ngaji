package com.jop.ngaji.network.api

import com.jop.ngaji.data.model.Prayer
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

class PrayerAPI (private val client: HttpClient){

    suspend fun getAllPrayer(): List<Prayer> {
        return client.get {
            url("doa")
        }.body()
    }
}