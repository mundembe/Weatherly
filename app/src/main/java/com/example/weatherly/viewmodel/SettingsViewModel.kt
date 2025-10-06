package com.example.weatherly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherly.data.datastore.SettingsDataStore
import com.example.weatherly.data.datastore.UserSettings
import com.example.weatherly.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repo: SettingsRepository) : ViewModel() {

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
    }
}
