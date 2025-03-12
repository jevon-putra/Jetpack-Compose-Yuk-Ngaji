package com.jop.ngaji.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LastReadSurah(
    val surahName: String = "الفاتحة",
    val surahNameLatin: String = "Al-Fatihah",
    val surahNumber: Int = 1,
    val ayahNumber: Int = 1,
)