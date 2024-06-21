package com.android.haivest.data

import com.android.haivest.data.network.detect.DetectService
import com.android.haivest.data.network.response.PredictResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File

class PredictRepository (
    private val detectService: DetectService
){

    suspend fun predictDisease(imageFile: File): Response<PredictResponse> {
        val requestBody = imageFile.asRequestBody("image/png".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("file", imageFile.name, requestBody)
        return detectService.predict(multipartBody)
    }


}