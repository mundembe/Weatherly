package com.example.weatherly.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

// Create a singleton DataStore instance
private val Context.dataStore by preferencesDataStore(name = "user_settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        private val LANGUAGE_KEY = stringPreferencesKey("language")
        private val UNIT_KEY = stringPreferencesKey("temperature_unit")
        private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications_enabled")
    }

    /** Flow of current settings */
    val settingsFlow: Flow<UserSettings> = context.dataStore.data.map { preferences ->
        UserSettings(
            language = preferences[LANGUAGE_KEY] ?: "en",
            temperatureUnit = preferences[UNIT_KEY] ?: "Celsius",
            notificationsEnabled = preferences[NOTIFICATIONS_KEY] ?: true
        )
    }

    /** Update functions */
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

    suspend fun updateNotifications(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[NOTIFICATIONS_KEY] = enabled
        }
    }
}
