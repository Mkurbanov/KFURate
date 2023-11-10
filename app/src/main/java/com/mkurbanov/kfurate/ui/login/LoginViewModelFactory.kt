package com.mkurbanov.kfurate.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mkurbanov.kfurate.data.local.UserPreferencesRepository
import com.mkurbanov.kfurate.data.repository.LoginRepository
import com.mkurbanov.kfurate.data.repository.TopStudentsRepository

class LoginViewModelFactory(private val repository: LoginRepository,private val prefRepository: UserPreferencesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(repository, prefRepository) as T
    }
}