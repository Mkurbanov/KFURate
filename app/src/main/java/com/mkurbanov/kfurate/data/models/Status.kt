package com.mkurbanov.kfurate.data.models

data class Status(val status: String) {
    fun isOK(): Boolean = status == Statuses.ok
}