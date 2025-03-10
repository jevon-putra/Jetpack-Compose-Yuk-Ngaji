package com.jop.ngaji.data.local.room.converter

import androidx.room.TypeConverter
import com.jop.ngaji.data.model.Surah
import kotlinx.serialization.json.Json

class ConverterAudioFull {
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromVariants(value: Surah.Ayah.AudioFull): String {
            return Json.encodeToString(value)
        }

        @TypeConverter
        @JvmStatic
        fun toVariants(value: String): Surah.Ayah.AudioFull {
            return Json.decodeFromString<Surah.Ayah.AudioFull>(value)
        }
    }
}