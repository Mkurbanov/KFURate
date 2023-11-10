package com.mkurbanov.kfurate.data.network

class ApiHelper(val apiService: ApiService) {
    suspend fun getStudents(token: String) = apiService.getStudents(token)

    suspend fun postLike(
        token: String,
        rtoken: String,
        likedStudenToken: String
    ) = apiService.postLike(token, rtoken, likedStudenToken)

    suspend fun getTopStudents(token: String) = apiService.getTopStudents(token)

    suspend fun login(ftoken: String) = apiService.login(ftoken)

    suspend fun reg(ftoken: String, name: String, gender: String) =
        apiService.reg(ftoken, name, gender)
}