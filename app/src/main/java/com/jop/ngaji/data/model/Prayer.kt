package com.jop.ngaji.data.model

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Entity(tableName = "doa")
@Serializable
@Immutable
data class Prayer(
    @PrimaryKey(autoGenerate = false)
    @SerialName("id")
    val id: Int = 0,
    @ColumnInfo("judul")
    @SerialName("judul")
    val judul: String = "",
    @ColumnInfo("latin")
    @SerialName("latin")
    val latin: String = "",
    @ColumnInfo("arab")
    @SerialName("arab")
    val arab: String = "",
    @ColumnInfo("terjemah")
    @SerialName("terjemah")
    val terjemah: String = "",
    @SerialName("favorite")
    @ColumnInfo("favorite")
    val favorite: Boolean = false,
)


