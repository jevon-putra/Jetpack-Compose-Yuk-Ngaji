package com.jop.ngaji.di

import androidx.media3.exoplayer.ExoPlayer
import org.koin.dsl.module

val exoPlayerModule = module {
    single<ExoPlayer> { ExoPlayer.Builder(get()).build() }
}
