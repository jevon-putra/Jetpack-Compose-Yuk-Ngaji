package com.jop.ngaji.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LastSyncLocation(
    val city: String = "Surabaya",
    val country: String = "Indonesia",
    val lastSync: Long = 0
)