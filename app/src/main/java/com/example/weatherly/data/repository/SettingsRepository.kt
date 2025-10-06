package com.example.weatherly.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherly.data.datastore.UserSettings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_settings")

class SettingsRepository(private val context: Context) {

    companion object {
        private val LANGUAGE_KEY = stringPreferencesKey("language")
        private val UNIT_KEY = stringPreferencesKey("unit")
        private val NOTIFICATION_KEY = booleanPreferencesKey("notifications")
    }

    val userSettings: Flow<UserSettings> = context.dataStore.data.map { prefs ->
        UserSettings(
            language = prefs[LANGUAGE_KEY] ?: "en",
            temperatureUnit = prefs[UNIT_KEY] ?: "Celsius",
            notificationsEnabled = prefs[NOTIFICATION_KEY] ?: true
        )
    }

    suspend fun updateLanguage(lang: String) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = lang
        }
    }

    suspend fun updateTemperatureUnit(unit: String) {
        context.dataStore.edit { prefs ->
            prefs[UNIT_KEY] = unit
        }
    }

    suspend fun toggleNotifications(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[NOTIFICATION_KEY] = enabled
        }
    }
}
