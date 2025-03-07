package com.jop.ngaji.room.converter

import androidx.room.TypeConverter
import com.jop.ngaji.data.model.Ayah
import kotlinx.serialization.json.Json

class ConverterListAyat {
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromVariants(value: List<Ayah>): String {
            return Json.encodeToString(value)
        }

        @TypeConverter
        @JvmStatic
        fun toVariants(value: String): List<Ayah> {
            return Json.decodeFromString<List<Ayah>>(value)
        }
    }
}