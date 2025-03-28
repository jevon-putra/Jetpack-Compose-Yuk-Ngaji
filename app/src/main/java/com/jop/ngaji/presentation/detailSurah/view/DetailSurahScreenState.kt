package com.jop.ngaji.presentation.detailSurah.view

import com.jop.ngaji.data.model.LastReadSurah
import com.jop.ngaji.data.model.Surah

data class DetailSurahScreenState(
    val isAllSurahLoading: Boolean = false,
    val isDetailSurahLoading: Boolean = false,

    val allSurah: List<Surah> = listOf(),

    val selectedSurah: Surah? = null,
    val isAudioPlaying: Boolean = false,
    val audioIndex: Int = 0,

    val selectedAyah: LastReadSurah? = null,
    val lastReadSurah: LastReadSurah = LastReadSurah(),

    val errorMessage: String? = null,
)