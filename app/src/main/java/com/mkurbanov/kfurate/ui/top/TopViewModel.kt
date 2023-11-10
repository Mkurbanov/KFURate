package com.mkurbanov.kfurate.ui.top

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mkurbanov.kfurate.data.models.TopStudent
import com.mkurbanov.kfurate.data.repository.TopStudentsRepository
import com.mkurbanov.kfurate.ui.utils.ErrorRes
import kotlinx.coroutines.launch

class TopViewModel(val repository: TopStudentsRepository) : ViewModel() {
    val error: MutableLiveData<ErrorRes> = MutableLiveData()
    private val _students: MutableLiveData<List<TopStudent>> = MutableLiveData(listOf())
    val students: MutableLiveData<List<TopStudent>> = _students

    fun getStudents(token: String) = viewModelScope.launch {
        try {
            _students.value = repository.getTopStudents(token)
        } catch (e: Exception){
            error.value = ErrorRes(e)
        }
    }

}