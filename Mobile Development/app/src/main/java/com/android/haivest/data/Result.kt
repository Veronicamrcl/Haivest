package com.android.haivest.data

data class Result<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null
)

//sealed class Result<out T : Any> {
//    data class Success<out T : Any>(val data: T) : Result<T>()
//    data class Error(val exception: String) : Result<Nothing>()
//}