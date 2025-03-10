package com.jop.ngaji.presentation.home.view

import com.jop.ngaji.data.model.LastReadSurah
import com.jop.ngaji.data.model.PrayTime

data class HomeScreenState(
    val isLoading: Boolean = false,
    val prayerTimes: PrayTime.Timings? = null,
    val lastReadSurah: LastReadSurah = LastReadSurah(),
)