package com.mkurbanov.kfurate.data.repository

import com.mkurbanov.kfurate.data.network.ApiHelper

class TopStudentsRepository(private val apiHelper: ApiHelper) {
    suspend fun getTopStudents(token: String) = apiHelper.getTopStudents(token)
}