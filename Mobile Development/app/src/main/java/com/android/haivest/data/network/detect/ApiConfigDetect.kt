package com.android.haivest.data.network.detect

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfigDetect {

    companion object {
        private fun provideOkHttpClient() : OkHttpClient {
            return OkHttpClient.Builder()
                .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
        }

        fun provideApiService() : DetectService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://haivest-ml-1-api-rkkc6rxfaa-et.a.run.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(provideOkHttpClient())
                .build()
            return retrofit.create(DetectService::class.java)
        }

    }

}