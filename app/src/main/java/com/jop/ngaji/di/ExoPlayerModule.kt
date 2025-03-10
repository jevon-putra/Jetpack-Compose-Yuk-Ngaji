package com.jop.ngaji.di

import com.jop.ngaji.util.ExoPlayerHelper
import org.koin.dsl.module

val exoPlayerModule = module {
    single<ExoPlayerHelper> { ExoPlayerHelper(get()) }
}
