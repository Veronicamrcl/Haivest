package com.android.haivest.ui.business.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.haivest.data.RecommendRepository
import com.android.haivest.data.Result
import com.android.haivest.data.network.response.RecommendResponse
import kotlinx.coroutines.launch

class RecommendViewModel(private val repository:RecommendRepository): ViewModel() {

    private val _recommendResult = MutableLiveData<Result<RecommendResponse>>()
    val recommendResult: LiveData<Result<RecommendResponse>> get() = _recommendResult

    fun recommendBusiness(city: String) {
        viewModelScope.launch {
            try {
                val response = repository.predict(city)
                if (response.isSuccessful) {
                    val recommendResponse = response.body()
                    _recommendResult.value = Result(success = true, data = recommendResponse)
                } else {
                    _recommendResult.value = Result(success = false, error = response.message())
                }
            } catch (e: Exception) {
                _recommendResult.value = Result(success = false, error = e.message.toString())
            }
        }
    }

}