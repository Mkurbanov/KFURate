package com.mkurbanov.kfurate.data.repository

import com.mkurbanov.kfurate.data.network.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getStudents(token: String) = apiHelper.getStudents(token)

    suspend fun postLike(
        token: String,
        rtoken: String,
        likedStudenToken: String
    ) = apiHelper.postLike(token, rtoken, likedStudenToken)
}