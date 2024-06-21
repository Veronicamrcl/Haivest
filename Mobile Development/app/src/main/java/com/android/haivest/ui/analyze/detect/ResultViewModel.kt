package com.android.haivest.ui.analyze.detect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.haivest.data.PredictRepository
import com.android.haivest.data.network.response.PredictResponse
import kotlinx.coroutines.launch
import java.io.File

class ResultViewModel(private val repository:PredictRepository):ViewModel() {

    private val _predictResult = MutableLiveData<Result<PredictResponse>>()
    val predictResult: LiveData<Result<PredictResponse>> get() = _predictResult

    fun predictDisease(imageFile: File) {
        viewModelScope.launch {
            try {
                val response = repository.predictDisease(imageFile)
                if (response.isSuccessful) {
                    _predictResult.value = Result.success(response.body()!!)
                } else {
                    _predictResult.value = Result.failure(Exception("Prediction failed"))
                }
            } catch (e: Exception) {
                _predictResult.value = Result.failure(e)
            }
        }
    }

}