package com.android.haivest.data.di

import android.content.Context
import com.android.haivest.data.MainRepository
import com.android.haivest.data.local.UserPreference
import com.android.haivest.data.local.dataStore
import com.android.haivest.data.network.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRepository(context: Context): MainRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }

       // val database = StoryDatabase.getDatabase(context)

        val apiService = ApiConfig.getApiService(user.token)
        return MainRepository.getInstance(apiService, pref)


    }

}