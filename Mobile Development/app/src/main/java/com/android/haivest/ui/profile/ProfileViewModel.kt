package com.android.haivest.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.haivest.data.MainRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: MainRepository):ViewModel() {

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}