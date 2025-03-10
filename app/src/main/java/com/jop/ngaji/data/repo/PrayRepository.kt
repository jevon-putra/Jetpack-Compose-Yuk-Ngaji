package com.jop.ngaji.data.repo

import com.jop.ngaji.data.Resource
import com.jop.ngaji.data.model.PrayTime
import com.jop.ngaji.network.api.PrayAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PrayRepository(private val prayAPI: PrayAPI) {

    fun getTime(date: String, city: String, country: String) : Flow<Resource<PrayTime.Timings>> = flow {
        try {
            emit(Resource.Loading())

            val request = prayAPI.getTime(date, city, country)
            emit(Resource.Success(data = request.data!!.timings))
        } catch (e: Exception){
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}