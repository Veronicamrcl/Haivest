package com.android.haivest.data.network.auth

import com.android.haivest.data.network.request.LoginRequest
import com.android.haivest.data.network.request.RegisterRequest
import com.android.haivest.data.network.response.LoginResponse
import com.android.haivest.data.network.response.NewsResponse
import com.android.haivest.data.network.response.ProfileResponse
import com.android.haivest.data.network.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {


    @POST("register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>


    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @GET("profile")
    suspend fun profile() : Response<ProfileResponse>

    @GET("news")
    suspend fun getNews(): Response<NewsResponse>

}