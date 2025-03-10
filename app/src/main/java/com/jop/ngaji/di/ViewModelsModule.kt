package com.jop.ngaji.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.jop.ngaji.data.local.store.DataStoreSetting
import com.jop.ngaji.presentation.detailSurah.viewModel.DetailSurahViewModel
import com.jop.ngaji.presentation.home.viewModel.HomeViewModel
import com.jop.ngaji.presentation.surah.viewModel.SurahViewModel
import com.jop.ngaji.util.dataStore
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    single<DataStore<Preferences>>{ get<Context>().dataStore }
    single<DataStoreSetting> { DataStoreSetting(get()) }

    viewModel<SurahViewModel>{ SurahViewModel(get()) }
    viewModel<HomeViewModel>{ HomeViewModel(get(), get(), get()) }
    viewModel<DetailSurahViewModel>{ DetailSurahViewModel(get(), get(), get()) }
}