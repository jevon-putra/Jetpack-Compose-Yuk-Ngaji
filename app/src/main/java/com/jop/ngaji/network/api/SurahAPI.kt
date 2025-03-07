package com.jop.ngaji.network.api

import com.jop.ngaji.data.BaseResponse
import com.jop.ngaji.data.model.Surah
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

class SurahAPI (private val client: HttpClient){

    suspend fun getAllSurah(): BaseResponse<List<Surah>> {
        return client.get { url("surat") }.body()
    }

    suspend fun getDetailSurah(surahNumber: Int): BaseResponse<Surah> {
        return client.get { url("surat/$surahNumber") }.body()
    }
}