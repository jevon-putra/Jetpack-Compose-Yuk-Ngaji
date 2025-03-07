package com.jop.ngaji.room.converter

import androidx.room.TypeConverter
import com.jop.ngaji.data.model.AudioFull
import kotlinx.serialization.json.Json

class ConverterAudioFull {
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromVariants(value: AudioFull): String {
            return Json.encodeToString(value)
        }

        @TypeConverter
        @JvmStatic
        fun toVariants(value: String): AudioFull {
            return Json.decodeFromString<AudioFull>(value)
        }
    }
}