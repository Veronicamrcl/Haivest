package com.android.haivest.data.network.response

data class ProfileResponse (
    val profile: Profile? = null
)

data class Profile (
    val id: Int? = null,
    val name: String? = null,
    val username: String? = null
)