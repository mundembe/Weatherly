package com.example.weatherly.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherly.data.datastore.SettingsDataStore
import com.example.weatherly.data.datastore.UserSettings
import com.example.weatherly.data.repository.SettingsRepository
import com.example.weatherly.utils.NotificationHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// FIX: Change to AndroidViewModel and pass Application in the constructor
class SettingsViewModel(
    application: Application,
    private val repo: SettingsRepository
) : AndroidViewModel(application) {

    val userSettings = repo.userSettings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserSettings())

    fun updateLanguage(lang: String) = viewModelScope.launch {
        repo.updateLanguage(lang)
    }

    fun updateTemperatureUnit(unit: String) = viewModelScope.launch {
        repo.updateTemperatureUnit(unit)
    }

    fun updateNotifications(enabled: Boolean) = viewModelScope.launch {
        repo.toggleNotifications(enabled)
        // FIX: If notifications are enabled, show the test notification
        if (enabled) {
            // Use the application context from AndroidViewModel
            NotificationHelper.showTestNotification(getApplication())
        }
    }
}
