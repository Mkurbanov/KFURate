package com.mkurbanov.kfurate.data.models

import com.google.gson.annotations.SerializedName

data class TopStudent(
    val token: String,
    val name: String,
    val image: String,
    @SerializedName("like_count")
    val likeCount: Int
)