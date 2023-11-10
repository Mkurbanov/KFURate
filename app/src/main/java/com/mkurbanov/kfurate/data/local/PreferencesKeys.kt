package com.mkurbanov.kfurate.data.local

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val NAME = stringPreferencesKey("name")
    val PHONE = stringPreferencesKey("phone")
    val TOKEN = stringPreferencesKey("token")
    val GENDER = stringPreferencesKey("gender")
}