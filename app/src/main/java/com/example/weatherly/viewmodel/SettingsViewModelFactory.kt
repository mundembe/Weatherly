package com.example.weatherly.viewmodel

import android.app.Application // <-- CHANGE from Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherly.data.repository.SettingsRepository

// FIX: The factory now needs the Application, not just a Context
class SettingsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            val repository = SettingsRepository(application)
            @Suppress("UNCHECKED_CAST")
            // FIX: Pass the application instance to the ViewModel
            return SettingsViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
