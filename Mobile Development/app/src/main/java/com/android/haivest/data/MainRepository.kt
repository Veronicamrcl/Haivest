package com.android.haivest.data

import com.android.haivest.data.local.UserPreference
import com.android.haivest.data.model.User
import com.android.haivest.data.network.auth.ApiService
import com.android.haivest.data.network.request.LoginRequest
import com.android.haivest.data.network.request.RegisterRequest
import com.android.haivest.data.network.response.LoginResponse
import com.android.haivest.data.network.response.RegisterResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class MainRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    ){

    suspend fun saveSession(user: User) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<User> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }


    suspend fun register(name: String, username: String,password: String): Response<RegisterResponse> {
        val registerRequest = RegisterRequest(name,username,password)
        return apiService.register(registerRequest)
    }



    suspend fun login(username: String, password: String): Response<LoginResponse> {
        val loginRequest = LoginRequest(username, password)
        return apiService.login(loginRequest)
    }


    companion object {
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,

            ): MainRepository =
            MainRepository(apiService, userPreference)
    }

    }