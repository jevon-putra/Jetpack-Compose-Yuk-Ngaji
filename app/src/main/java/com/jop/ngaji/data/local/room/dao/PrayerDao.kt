package com.jop.ngaji.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jop.ngaji.data.model.Prayer

@Dao
interface PrayerDao {
    @Query("SELECT * FROM `doa`")
    suspend fun getAllPrayer(): List<Prayer>

    @Query("SELECT * FROM `doa` WHERE `favorite` = 1")
    suspend fun getAllFavouriteDoa(): List<Prayer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listSurah: List<Prayer>)
}