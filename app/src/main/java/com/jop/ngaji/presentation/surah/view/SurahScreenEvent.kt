package com.jop.ngaji.presentation.surah.view

sealed interface SurahScreenEvent {
    data object GetAllSurah: SurahScreenEvent
    data class SearchSurah(val keyword: String): SurahScreenEvent
}