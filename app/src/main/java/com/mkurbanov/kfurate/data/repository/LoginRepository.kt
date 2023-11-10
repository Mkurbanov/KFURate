package com.mkurbanov.kfurate.data.repository

import com.mkurbanov.kfurate.data.network.ApiHelper

class LoginRepository(private val apiHelper: ApiHelper) {
    suspend fun login(ftoken: String) = apiHelper.login(ftoken)

    suspend fun reg(ftoken: String, name: String, gender: String) =
        apiHelper.reg(ftoken, name, gender)
}