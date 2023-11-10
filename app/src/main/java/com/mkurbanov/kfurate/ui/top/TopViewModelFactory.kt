package com.mkurbanov.kfurate.ui.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mkurbanov.kfurate.data.repository.TopStudentsRepository

class TopViewModelFactory(private val repository: TopStudentsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TopViewModel(repository) as T
    }
}