package com.mkurbanov.kfurate.ui.utils

import android.util.Log
import com.mkurbanov.kfurate.data.config.LOG_TAG
import java.net.SocketTimeoutException

class ErrorRes(private val e:Exception? = null) {
    val message:String
        get() {
            when (e) {
                null -> return "error"
                is SocketTimeoutException -> {
                    val m = "Network error"
                    Log.e(LOG_TAG, m)
                    return m
                }
                else -> {
                    Log.e(LOG_TAG, e.toString())
                    return "error"
                }
            }
        }
}