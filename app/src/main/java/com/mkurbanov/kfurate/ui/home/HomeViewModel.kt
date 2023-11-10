package com.mkurbanov.kfurate.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mkurbanov.kfurate.data.models.MainResponse
import com.mkurbanov.kfurate.data.repository.MainRepository
import com.mkurbanov.kfurate.ui.utils.ErrorRes
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MainRepository) : ViewModel() {
    val error: MutableLiveData<ErrorRes> = MutableLiveData()
    val students: MutableLiveData<MainResponse> = MutableLiveData()
    val likedStatus: MutableLiveData<String> = MutableLiveData()

    fun getStudents(token: String) = viewModelScope.launch {
        try {
            students.value = repository.getStudents(token)
        } catch (e: Exception) {
            error.value = ErrorRes(e)
        }
    }

    fun postLike(
        token: String, studentPos: Int
    ) {
        viewModelScope.launch {
            try {
                val rtoken =  students.value!!.rtoken
                val likedStudenToken =  students.value!!.students[studentPos].token
                val status = repository.postLike(token, rtoken, likedStudenToken)
                if (status.isOK())
                    likedStatus.value = status.status
                else error.value = ErrorRes()
            } catch (e: Exception) {
                error.value = ErrorRes(e)
            }
        }
    }

}