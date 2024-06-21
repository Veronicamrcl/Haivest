package com.android.haivest.data.network.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecommendRequest (
    val city: String
) : Parcelable