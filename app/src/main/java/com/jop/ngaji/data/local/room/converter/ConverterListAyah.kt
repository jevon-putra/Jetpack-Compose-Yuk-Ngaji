package com.jop.ngaji.data.local.room.converter

import androidx.room.TypeConverter
import com.jop.ngaji.data.model.Surah
import kotlinx.serialization.json.Json

class ConverterListAyah {
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromVariants(value: List<Surah.Ayah>): String {
            return Json.encodeToString(value)
        }

        @TypeConverter
        @JvmStatic
        fun toVariants(value: String): List<Surah.Ayah> {
            return Json.decodeFromString<List<Surah.Ayah>>(value)
        }
    }
}