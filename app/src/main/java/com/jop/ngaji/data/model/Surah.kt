package com.jop.ngaji.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Entity(tableName = "surah")
@Serializable
data class Surah(
    @PrimaryKey(autoGenerate = false)
    @SerialName("nomor")
    val id: Int = 0,
    @ColumnInfo("nama")
    @SerialName("nama")
    val nama: String = "",
    @ColumnInfo("namaLatin")
    @SerialName("namaLatin")
    val namaLatin: String = "",
    @ColumnInfo("jumlahAyat")
    @SerialName("jumlahAyat")
    val jumlahAyat: Int = 0,
    @ColumnInfo("tempatTurun")
    @SerialName("tempatTurun")
    val tempatTurun: String = "",
    @SerialName("arti")
    @ColumnInfo("arti")
    val arti: String = "",
    @SerialName("favorite")
    @ColumnInfo("favorite")
    var favorite: Boolean = false,
    @ColumnInfo("ayat")
    @SerialName("ayat")
    val ayat: List<Ayah> = listOf()
)

@Serializable
data class AudioFull(
    @SerialName("04")
    val x04: String = "",
)

@Serializable
data class Ayah(
    @SerialName("nomorAyat")
    val nomorAyat: Int = 0,
    @SerialName("teksArab")
    val teksArab: String = "",
    @SerialName("teksLatin")
    val teksLatin: String = "",
    @SerialName("teksIndonesia")
    val teksIndonesia: String = "",
    @SerialName("audio")
    val audio: AudioFull = AudioFull()
)

