package com.android.haivest.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.android.haivest.data.MainRepository
import com.android.haivest.data.model.User

class SplashViewModel(private val repository: MainRepository):ViewModel() {

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

}