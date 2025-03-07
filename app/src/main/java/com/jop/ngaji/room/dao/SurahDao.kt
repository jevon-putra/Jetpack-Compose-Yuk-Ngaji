package com.jop.ngaji.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jop.ngaji.data.model.Surah

@Dao
interface SurahDao {
    @Query("SELECT * FROM `surah`")
    suspend fun getAllSurah(): List<Surah>

    @Query("SELECT * FROM `surah` WHERE `id` = :surahNumber LIMIT 1")
    suspend fun detailSurah(surahNumber: Int): List<Surah>

    @Update
    suspend fun update(surah: Surah)

    @Query("UPDATE `surah` SET `favorite` = :isFavorite WHERE `id` = :surahNumber")
    suspend fun updateFavorite(surahNumber: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listSurah: List<Surah>)
}