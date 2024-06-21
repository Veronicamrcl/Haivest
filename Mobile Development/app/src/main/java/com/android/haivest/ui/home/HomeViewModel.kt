package com.android.haivest.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.android.haivest.data.MainRepository
import com.android.haivest.data.model.User
import com.android.haivest.data.network.auth.ApiConfig
import com.android.haivest.data.network.response.NewsResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MainRepository):ViewModel() {

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

    private val _newsData = MutableLiveData<NewsResponse>()
    val newsData: LiveData<NewsResponse> get() = _newsData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val newsApiService = ApiConfig.getNewsApiService()

    fun fetchNewsData() {
        _isLoading.value = true
        viewModelScope.launch {
            val response = newsApiService.getNews()
            if (response.isSuccessful) {
                _newsData.postValue(response.body())
            }
            _isLoading.value = false
        }
    }

}