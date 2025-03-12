package com.jop.ngaji.presentation.prayer.view

import com.jop.ngaji.data.model.Prayer

data class PrayerScreenState(
    val isLoading: Boolean = false,
    val selectedPrayer: Prayer? = null,
    val data: List<Prayer> = listOf(),
    val errorMessage: String? = null
)