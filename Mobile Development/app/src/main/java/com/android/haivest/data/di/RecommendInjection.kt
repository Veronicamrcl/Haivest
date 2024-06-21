package com.android.haivest.data.di

import android.content.Context
import com.android.haivest.data.PredictRepository
import com.android.haivest.data.RecommendRepository
import com.android.haivest.data.network.detect.ApiConfigDetect
import com.android.haivest.data.network.recommend.ApiConfigRecommend

object RecommendInjection {

    fun provideRepository(context: Context): RecommendRepository {

        val recommendService = ApiConfigRecommend.provideApiService()
        // val pref = UserPreference.getInstance(context.dataStore)
        // val user = runBlocking { pref.getSession().first() }

        return RecommendRepository(recommendService)
    }

}