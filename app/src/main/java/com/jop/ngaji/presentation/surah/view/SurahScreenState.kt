package com.jop.ngaji.presentation.surah.view

import com.jop.ngaji.data.model.Surah

data class SurahScreenState(
    val isLoading: Boolean = false,
    val data: List<Surah> = listOf(),
    val errorMessage: String? = null
)