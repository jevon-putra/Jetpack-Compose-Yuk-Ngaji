package com.jop.ngaji.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jop.ngaji.data.model.Surah
import com.jop.ngaji.data.local.room.converter.ConverterAudioFull
import com.jop.ngaji.data.local.room.converter.ConverterListAyah
import com.jop.ngaji.data.local.room.dao.PrayerDao
import com.jop.ngaji.data.local.room.dao.SurahDao
import com.jop.ngaji.data.model.Prayer

@Database(entities = [Surah::class, Prayer::class], version = 1, exportSchema = false)
@TypeConverters(ConverterListAyah::class, ConverterAudioFull::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun surahDao(): SurahDao
    abstract fun prayerDao(): PrayerDao
}