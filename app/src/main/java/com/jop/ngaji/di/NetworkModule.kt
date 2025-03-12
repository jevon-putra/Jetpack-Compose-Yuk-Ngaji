package com.jop.ngaji.di

import com.jop.ngaji.network.NetworkClient
import com.jop.ngaji.network.api.PrayAPI
import com.jop.ngaji.network.api.PrayerAPI
import com.jop.ngaji.network.api.SurahAPI
import org.koin.dsl.module

val networkModule = module {
    single<NetworkClient> { NetworkClient() }

    single { SurahAPI(get<NetworkClient>().quranClient()) }
    single { PrayAPI(get<NetworkClient>().prayClient()) }
    single { PrayerAPI(get<NetworkClient>().prayerClient()) }
}