package com.android.haivest.data.network.response


data class LoginResponse (
    val message: String,
    val accessToken: String,
    val refreshToken: String
)