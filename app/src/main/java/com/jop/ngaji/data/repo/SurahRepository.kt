package com.jop.ngaji.data.repo

import com.jop.ngaji.data.Resource
import com.jop.ngaji.data.model.Surah
import com.jop.ngaji.network.api.SurahAPI
import com.jop.ngaji.room.dao.SurahDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SurahRepository(private val surahDao: SurahDao, private val surahAPI: SurahAPI) {
    fun allSurah() : Flow<Resource<List<Surah>>> = flow {
        try {
            emit(Resource.Loading())

            val surahLocal = surahDao.getAllSurah()
            if(surahLocal.isEmpty()) {
                val request = surahAPI.getAllSurah()
                emit(Resource.Success(data = request.data ?: mutableListOf()))
                surahDao.insertAll(request.data ?: mutableListOf())
            } else {
                emit(Resource.Success(data = surahLocal))
            }
        } catch (e: Exception){
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    fun detailSurah(surahNumber: Int) : Flow<Resource<Surah?>> = flow {
        try {
            emit(Resource.Loading())

            val detailSurah = surahDao.detailSurah(surahNumber).first()
            if(detailSurah.ayat.isEmpty()) {
                val request = surahAPI.getDetailSurah(surahNumber)
                emit(Resource.Success(data = request.data))
                surahDao.update(request.data!!)
            } else {
                emit(Resource.Success(data = detailSurah))
            }
        } catch (e: Exception){
            emit(Resource.Error(message = e.message.toString()))
        }
    }

   suspend fun updateFavoriteSurah(surahNumber: Int, isFavorite: Boolean) = surahDao.updateFavorite(surahNumber, isFavorite)
}