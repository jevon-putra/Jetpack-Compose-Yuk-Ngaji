package com.jop.ngaji.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
        @SerialName("status")
        val status: Boolean? = false,
        @SerialName("message")
        var message: String? = "",
        @SerialName("data")
        var data: T? = null
)