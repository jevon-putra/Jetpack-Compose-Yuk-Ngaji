package com.jop.ngaji.di

import com.jop.ngaji.presentation.detailSurah.viewModel.DetailSurahViewModel
import com.jop.ngaji.presentation.surah.viewModel.SurahViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel<SurahViewModel>{ SurahViewModel(get()) }
    viewModel<DetailSurahViewModel>{ DetailSurahViewModel(get(), get()) }
}