package com.android.haivest.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.haivest.data.MainRepository
import kotlinx.coroutines.launch
import com.android.haivest.data.Result
import com.android.haivest.data.model.User
import com.android.haivest.data.network.response.LoginResponse

class LoginViewModel(private val repository: MainRepository):ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> get() = _loginResult

    fun saveSession(user: User) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(username, password)
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    _loginResult.value = Result(success = true, data = loginResponse)
                } else {
                    _loginResult.value = Result(success = false, error = response.message())
                }
            } catch (e: Exception) {
                _loginResult.value = Result(success = false, error = e.message.toString())
            }
        }
    }
}