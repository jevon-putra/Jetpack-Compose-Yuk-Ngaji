package com.jop.ngaji.data

sealed class Resource<T>(val data: T? = null, val message: String? = null, val type: String? = null) {
    class Loading<T>(type: String = "") : Resource<T>(null, "", type)
    class Success<T>(data: T, message: String = "", type: String = "") : Resource<T>(data, message, type)
    class Error<T>(message: String, data: T? = null, type: String = "") : Resource<T>(data, message, type)
}