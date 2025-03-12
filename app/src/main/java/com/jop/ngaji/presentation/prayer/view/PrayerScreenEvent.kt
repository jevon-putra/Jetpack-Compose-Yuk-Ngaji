package com.jop.ngaji.presentation.prayer.view

import com.jop.ngaji.data.model.Prayer

sealed interface PrayerScreenEvent {
    data class ShowDetailPrayer(val prayer: Prayer?): PrayerScreenEvent
}