package com.android.haivest.data.network.response

import com.google.gson.annotations.SerializedName


data class PredictResponse (
    @SerializedName("cause_disease")
    val causeDisease: String,
    val message: String,
    val symptoms: List<String>,
    val tips: List<String>
)
