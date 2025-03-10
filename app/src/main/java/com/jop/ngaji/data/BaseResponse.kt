package com.jop.ngaji.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
        @SerialName("message")
        var message: String? = "",
        @SerialName("data")
        var data: T? = null
)