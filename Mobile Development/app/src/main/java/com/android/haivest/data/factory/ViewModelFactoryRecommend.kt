package com.android.haivest.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.haivest.data.RecommendRepository
import com.android.haivest.data.di.RecommendInjection
import com.android.haivest.ui.business.recommend.RecommendViewModel

class ViewModelFactoryRecommend (private val repository: RecommendRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RecommendViewModel::class.java) -> {
                RecommendViewModel(repository) as T
            }


            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        private var instance: ViewModelFactoryRecommend? = null

        fun getInstance(context: Context): ViewModelFactoryRecommend {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactoryRecommend(RecommendInjection.provideRepository(context)).also { instance = it }
            }
        }
    }

}