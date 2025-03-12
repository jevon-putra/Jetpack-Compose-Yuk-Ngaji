package com.jop.ngaji.di

import com.jop.ngaji.data.repo.PrayRepository
import com.jop.ngaji.data.repo.PrayerRepository
import com.jop.ngaji.data.repo.SurahRepository
import org.koin.dsl.module

val repositoriesModule = module {
    single<SurahRepository>{ SurahRepository(get(), get()) }
    single<PrayRepository>{ PrayRepository(get()) }
    single<PrayerRepository>{ PrayerRepository(get(), get()) }
}