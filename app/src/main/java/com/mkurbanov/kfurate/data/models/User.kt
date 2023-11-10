package com.mkurbanov.kfurate.data.models

import retrofit2.http.Field

data class User(val name:String, val phone:String, val token:String, @Field("gender") val gender:String)