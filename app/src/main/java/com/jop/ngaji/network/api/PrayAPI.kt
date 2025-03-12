package com.jop.ngaji.network.api

import com.jop.ngaji.data.BaseResponse
import com.jop.ngaji.data.model.pray.PrayTime
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

class PrayAPI (private val client: HttpClient){

    suspend fun getTime(date: String, city: String, country: String): BaseResponse<PrayTime> {
        return client.get {
            url("timingsByCity/${date}?city=${city}&country=${country}")
        }.body()
    }
}