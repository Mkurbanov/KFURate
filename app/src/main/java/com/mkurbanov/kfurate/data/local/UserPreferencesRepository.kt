package com.mkurbanov.kfurate.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.lifecycle.asLiveData
import com.mkurbanov.kfurate.data.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>,
    context: Context
) {
    val userPreferencesFlow: Flow<UserSharedPref> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val name = preferences[PreferencesKeys.NAME]?: ""
            val phone = preferences[PreferencesKeys.PHONE]?: ""
            val token = preferences[PreferencesKeys.TOKEN]?: ""
            val gender = preferences[PreferencesKeys.GENDER]?: ""
            UserSharedPref(name, phone, token, gender)
        }

    suspend fun updateUser(user: User) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NAME] = user.name
            preferences[PreferencesKeys.PHONE] = user.phone
            preferences[PreferencesKeys.TOKEN] = user.token
            preferences[PreferencesKeys.GENDER] = user.gender
        }
    }

}