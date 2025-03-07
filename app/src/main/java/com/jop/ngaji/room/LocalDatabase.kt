package com.jop.ngaji.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jop.ngaji.data.model.Surah
import com.jop.ngaji.room.converter.ConverterAudioFull
import com.jop.ngaji.room.converter.ConverterListAyat
import com.jop.ngaji.room.dao.SurahDao

@Database(entities = [Surah::class], version = 1, exportSchema = false)
@TypeConverters(ConverterListAyat::class, ConverterAudioFull::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun surahDao(): SurahDao
}