package com.jop.ngaji.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LastSyncLocation(
    val city: String = "",
    val country: String = "",
    val lastSync: Long = 0
)