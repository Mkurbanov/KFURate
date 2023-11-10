package com.mkurbanov.kfurate.data.network

import com.mkurbanov.kfurate.data.models.LoginResponse
import com.mkurbanov.kfurate.data.models.MainResponse
import com.mkurbanov.kfurate.data.models.RegResponse
import com.mkurbanov.kfurate.data.models.Status
import com.mkurbanov.kfurate.data.models.TopStudent
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @POST("login.php")
    @FormUrlEncoded
    suspend fun login(@Field("ftoken") ftoken: String): LoginResponse

    @POST("reg.php")
    @FormUrlEncoded
    suspend fun reg(
        @Field("ftoken") ftoken: String,
        @Field("name") name: String,
        @Field("gender") gender: String
    ): RegResponse

    @POST("home.php")
    @FormUrlEncoded
    suspend fun getStudents(@Field("token") token: String): MainResponse

    @POST("like.php")
    @FormUrlEncoded
    suspend fun postLike(
        @Field("token") token: String,
        @Field("rtoken") rtoken: String,
        @Field("liked") likedStudentToken: String
    ): Status

    @POST("top_students.php")
    @FormUrlEncoded
    suspend fun getTopStudents(@Field("token") token: String): List<TopStudent>
}