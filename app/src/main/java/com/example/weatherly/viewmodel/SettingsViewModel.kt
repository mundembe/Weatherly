package com.example.weatherly.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherly.data.repository.SettingsRepository
import com.example.weatherly.data.datastore.UserSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SettingsRepository(application)

    // Collects settings as StateFlow for easy observation in Compose
    val userSettings: StateFlow<UserSettings> = repo.userSettings
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UserSettings()
        )

    fun updateLanguage(lang: String) = viewModelScope.launch {
        repo.updateLanguage(lang)
    }

    fun updateTemperatureUnit(unit: String) = viewModelScope.launch {
        repo.updateTemperatureUnit(unit)
    }

    fun updateNotifications(enabled: Boolean) = viewModelScope.launch {
        repo.updateNotifications(enabled)
    }
}
