package com.jop.ngaji

import android.app.Application
import com.jop.ngaji.di.exoPlayerModule
import com.jop.ngaji.di.networkModule
import com.jop.ngaji.di.repositoriesModule
import com.jop.ngaji.di.roomModule
import com.jop.ngaji.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class YukNgaji : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@YukNgaji)
            androidLogger()
            modules(
                listOf(
                    exoPlayerModule,
                    viewModelsModule,
                    repositoriesModule,
                    networkModule,
                    roomModule
                )
            )
        }
    }
}