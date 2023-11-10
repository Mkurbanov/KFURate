package com.mkurbanov.kfurate.data.models

data class Gender(val gender: String) {
    fun isMan(): Boolean = gender == Genders.man
    fun isWoman(): Boolean = gender == Genders.woman
}