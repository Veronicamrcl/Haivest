package com.android.haivest.data.network.recommend

import com.android.haivest.data.network.request.LoginRequest
import com.android.haivest.data.network.request.RecommendRequest
import com.android.haivest.data.network.response.LoginResponse
import com.android.haivest.data.network.response.RecommendResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RecommendService {

    @POST("predict")
    suspend fun predict(
        @Body recommendRequest: RecommendRequest
    ): Response<RecommendResponse>

}