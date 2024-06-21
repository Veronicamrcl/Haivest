package com.android.haivest.data

import com.android.haivest.data.network.detect.DetectService
import com.android.haivest.data.network.recommend.RecommendService
import com.android.haivest.data.network.request.LoginRequest
import com.android.haivest.data.network.request.RecommendRequest
import com.android.haivest.data.network.response.LoginResponse
import com.android.haivest.data.network.response.RecommendResponse
import retrofit2.Response

class RecommendRepository(
    private val recommendService: RecommendService
) {

    suspend fun predict(city: String): Response<RecommendResponse> {
        val loginRequest = RecommendRequest(city)
        return recommendService.predict(loginRequest)
    }
}