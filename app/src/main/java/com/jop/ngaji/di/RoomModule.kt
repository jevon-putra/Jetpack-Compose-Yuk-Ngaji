package com.jop.ngaji.di

import androidx.room.Room
import com.jop.ngaji.room.LocalDatabase
import com.jop.ngaji.room.dao.SurahDao
import org.koin.dsl.module

val roomModule = module {
    single<LocalDatabase> {
        Room.databaseBuilder(get(), LocalDatabase::class.java, "ngaji_yuk")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<SurahDao> { get<LocalDatabase>().surahDao() }
}