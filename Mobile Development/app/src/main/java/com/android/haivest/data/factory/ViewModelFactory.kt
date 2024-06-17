package com.android.haivest.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.haivest.data.MainRepository
import com.android.haivest.data.di.Injection
import com.android.haivest.ui.auth.login.LoginViewModel
import com.android.haivest.ui.auth.register.RegisterViewModel
import com.android.haivest.ui.home.HomeViewModel
import com.android.haivest.ui.profile.ProfileViewModel
import com.android.haivest.ui.splash.SplashViewModel

class ViewModelFactory (private val repository: MainRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(repository) as T
            }


            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context)).also { instance = it }
            }
        }
    }

}