package com.example.weatherly.data.repository

import android.content.Context
import com.example.weatherly.data.datastore.SettingsDataStore
import kotlinx.coroutines.flow.Flow

class SettingsRepository(context: Context) {

    private val dataStore = SettingsDataStore(context)

    val userSettings = dataStore.settingsFlow

    suspend fun updateLanguage(lang: String) = dataStore.updateLanguage(lang)

    suspend fun updateTemperatureUnit(unit: String) = dataStore.updateTemperatureUnit(unit)

    suspend fun updateNotifications(enabled: Boolean) = dataStore.updateNotifications(enabled)
}
