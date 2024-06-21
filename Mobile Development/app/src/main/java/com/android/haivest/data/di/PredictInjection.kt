package com.android.haivest.data.di

import android.content.Context
import com.android.haivest.data.PredictRepository
import com.android.haivest.data.network.detect.ApiConfigDetect


object PredictInjection {

     fun provideRepository(context: Context): PredictRepository {

         val detectService = ApiConfigDetect.provideApiService()
         // val pref = UserPreference.getInstance(context.dataStore)
         // val user = runBlocking { pref.getSession().first() }

         return PredictRepository(detectService)
     }

}