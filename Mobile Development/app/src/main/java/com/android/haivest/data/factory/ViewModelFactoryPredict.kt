package com.android.haivest.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.haivest.data.PredictRepository
import com.android.haivest.data.di.PredictInjection
import com.android.haivest.ui.analyze.detect.ResultViewModel

class ViewModelFactoryDetect (private val repository: PredictRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ResultViewModel::class.java) -> {
                ResultViewModel(repository) as T
            }


            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        private var instance: ViewModelFactoryDetect? = null

        fun getInstance(context: Context): ViewModelFactoryDetect {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactoryDetect(PredictInjection.provideRepository(context)).also { instance = it }
            }
        }
    }

}