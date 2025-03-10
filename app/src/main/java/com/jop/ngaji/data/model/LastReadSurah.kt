package com.jop.ngaji.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LastReadSurah(
    val surahName: String = "Al-Fatihah",
    val surahNameLatin: String = "الفاتحة",
    val surahNumber: Int = 1,
    val ayahNumber: Int = 1,
)