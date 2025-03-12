package com.jop.ngaji.presentation.home.view

import com.jop.ngaji.data.model.pray.DetailPrayTime
import com.jop.ngaji.data.model.LastReadSurah
import com.jop.ngaji.data.model.LastSyncLocation

data class HomeScreenState(
    val isLoading: Boolean = true,
    val prayerTimes: List<DetailPrayTime> = listOf(),
    val lastReadSurah: LastReadSurah = LastReadSurah(),
    val lastSyncLocation: LastSyncLocation = LastSyncLocation(),
)