package com.jop.ngaji.di

import com.jop.ngaji.data.repo.SurahRepository
import org.koin.dsl.module

val repositoriesModule = module {
    single<SurahRepository>{ SurahRepository(get(), get()) }
}