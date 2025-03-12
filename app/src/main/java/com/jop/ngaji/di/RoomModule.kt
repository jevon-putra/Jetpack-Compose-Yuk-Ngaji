package com.jop.ngaji.di

import androidx.room.Room
import com.jop.ngaji.data.local.room.LocalDatabase
import com.jop.ngaji.data.local.room.dao.PrayerDao
import com.jop.ngaji.data.local.room.dao.SurahDao
import org.koin.dsl.module

val roomModule = module {
    single<LocalDatabase> {
        Room.databaseBuilder(get(), LocalDatabase::class.java, "ngaji_yuk")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<SurahDao> { get<LocalDatabase>().surahDao() }
    single<PrayerDao> { get<LocalDatabase>().prayerDao() }
}