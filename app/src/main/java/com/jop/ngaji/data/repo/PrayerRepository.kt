package com.jop.ngaji.data.repo

import com.jop.ngaji.data.Resource
import com.jop.ngaji.data.local.room.dao.PrayerDao
import com.jop.ngaji.data.model.Prayer
import com.jop.ngaji.data.model.Surah
import com.jop.ngaji.network.api.PrayerAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PrayerRepository(private val prayerDao: PrayerDao, private val prayerAPI: PrayerAPI) {

    fun getAllPrayer() : Flow<Resource<List<Prayer>>> = flow {
        try {
            emit(Resource.Loading())

            val prayerLocal = prayerDao.getAllPrayer()
            if(prayerLocal.isEmpty()) {
                val request = prayerAPI.getAllPrayer()
                emit(Resource.Success(data = request))
                prayerDao.insertAll(request)
            } else {
                emit(Resource.Success(data = prayerLocal))
            }
        } catch (e: Exception){
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    fun getAllPrayerFavorite() : Flow<Resource<List<Prayer>>> = flow {
        try {
            emit(Resource.Loading())

            val detailSurah = prayerDao.getAllFavouriteDoa()
            emit(Resource.Success(data = detailSurah))
        } catch (e: Exception){
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}