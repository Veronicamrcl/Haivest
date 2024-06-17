package com.android.haivest.ui.auth.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.haivest.data.MainRepository
import com.android.haivest.data.network.response.RegisterResponse
import kotlinx.coroutines.launch
import com.android.haivest.data.Result
import com.android.haivest.data.network.request.RegisterRequest

class RegisterViewModel(private val repository: MainRepository): ViewModel() {

    private val _registrationResult = MutableLiveData<Result<RegisterResponse>>()
    val registrationResult: LiveData<Result<RegisterResponse>> get() = _registrationResult

    fun register(name: String, username:String,password: String) {
        viewModelScope.launch {
            try {
                val response = repository.register(name,username, password)
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    _registrationResult.value = Result(success = true, data = registerResponse)
                } else {
                    _registrationResult.value = Result(success = false, error = response.message())
                }
            } catch (e: Exception) {
                _registrationResult.value = Result(success = false, error = e.message.toString())
            }
        }
    }

}