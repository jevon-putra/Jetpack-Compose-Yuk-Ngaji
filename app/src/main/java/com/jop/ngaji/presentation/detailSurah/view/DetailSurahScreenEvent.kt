package com.jop.ngaji.presentation.detailSurah.view

import com.jop.ngaji.data.model.LastReadSurah

sealed interface DetailSurahScreenEvent {
    data class GetDetailSurah(val surahNumber: Int): DetailSurahScreenEvent
    data class SetFavoriteSurah(val surahNumber: Int, val isFavorite: Boolean): DetailSurahScreenEvent
    data class ShowDetailAyahBottomSheet(val selectedAyah: LastReadSurah?): DetailSurahScreenEvent
    data class SetLastReadSurah(val selectedAyah: LastReadSurah): DetailSurahScreenEvent

    data class OnPlayAudioStartFromAyah(val ayahIndex: Int): DetailSurahScreenEvent
    data object OnPlayAudio: DetailSurahScreenEvent
    data object OnPauseAudio: DetailSurahScreenEvent
}