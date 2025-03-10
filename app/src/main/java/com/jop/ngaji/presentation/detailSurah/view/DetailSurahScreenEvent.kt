package com.jop.ngaji.presentation.detailSurah.view

sealed interface DetailSurahScreenEvent {
    data class GetDetailSurah(val surahNumber: Int): DetailSurahScreenEvent
    data class OnFavoriteSurah(val surahNumber: Int, val isFavorite: Boolean): DetailSurahScreenEvent
    data object OnPlayAudio: DetailSurahScreenEvent
    data object OnPauseAudio: DetailSurahScreenEvent
}