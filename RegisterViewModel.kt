package com.example.storyapp.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.AppRepository

class RegisterViewModel(private val repository: AppRepository) :
    ViewModel() {
    val isLoading = MutableLiveData(false)
    val errorMessage = MutableLiveData<String>()

    fun register(name: String, email: String, password: String) =
        repository.register(name, email, password)
}