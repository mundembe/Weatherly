package com.example.weatherly.viewmodel

import android.app.Application // <-- IMPORT THIS
import androidx.lifecycle.AndroidViewModel // <-- IMPORT THIS and remove the standard ViewModel import
import androidx.lifecycle.viewModelScope
import com.example.weatherly.data.db.WeatherEntity
import com.example.weatherly.data.repository.SettingsRepository
import com.example.weatherly.data.repository.WeatherRepository
import com.example.weatherly.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue // Add these if not already present
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


class WeatherViewModel(
    application: Application,
    private val repo: WeatherRepository,
    private val apiKey: String
) : AndroidViewModel(application) {

    private val settingsRepo =
        SettingsRepository(application)
    var tempUnit by mutableStateOf("Celsius")
        private set

    init {
        viewModelScope.launch {
            settingsRepo.userSettings.collect { settings ->
                tempUnit = settings.temperatureUnit
            }
        }
    }

    /** helper to format temp for UI **/
    fun formatTemperature(tempInCelsius: Double): String {
        return if (tempUnit == "Fahrenheit") {
            val f = tempInCelsius * 9.0 / 5.0 + 32.0
            String.format("%.1f°F", f)
        } else {
            String.format("%.1f°C", tempInCelsius)
        }
    }

    // Expose a StateFlow that emits Resource states
    private val _weatherState = MutableStateFlow<Resource<WeatherEntity>>(Resource.Loading)
    val weatherState = _weatherState.asStateFlow()

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            // Immediately emit Loading state
            _weatherState.value = Resource.Loading
            try {
                // Get weather from the repository
                val result = repo.getWeather(city, apiKey)
                // If the result is not null, emit Success, otherwise emit Error
                if (result != null) {
                    _weatherState.value = Resource.Success(result)
                } else {
                    _weatherState.value = Resource.Error("Could not fetch weather for $city.")
                }
            } catch (e: Exception) {
                // Catch any exceptions and emit an Error state
                _weatherState.value = Resource.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }
}

