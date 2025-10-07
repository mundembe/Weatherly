package com.example.weatherly.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherly.data.db.WeatherEntity
import com.example.weatherly.data.repository.SettingsRepository
import com.example.weatherly.data.repository.WeatherRepository
import com.example.weatherly.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class WeatherViewModel(
    application: Application,
    private val repo: WeatherRepository,
    private val apiKey: String
) : AndroidViewModel(application) {

    private val _weatherState = MutableStateFlow<Resource<WeatherEntity>>(Resource.Loading)
    val weatherState = _weatherState.asStateFlow()

    private val _cityQuery = mutableStateOf("Cape Town") // Default city
    val cityQuery: State<String> = _cityQuery

    var tempUnit by mutableStateOf("Celsius")
        private set
    private val settingsRepo = SettingsRepository(application)

    init {
        viewModelScope.launch {
            settingsRepo.userSettings.collect { settings ->
                tempUnit = settings.temperatureUnit
            }
        }
    }

    fun updateCityQuery(newQuery: String) {
        _cityQuery.value = newQuery
    }

    fun fetchWeather() {
        val cityToFetch = _cityQuery.value.trim()
        if (cityToFetch.isBlank()) {
            _weatherState.value = Resource.Error("City name cannot be empty.")
            return
        }

        viewModelScope.launch {
            _weatherState.value = Resource.Loading
            try {
                // REVISED: Handle nullable result from repository
                val result: WeatherEntity? = repo.getWeather(cityToFetch, apiKey)
                if (result != null) {
                    _weatherState.value = Resource.Success(result)
                } else {
                    // FIX: Provide a more specific error message if nothing is found
                    _weatherState.value = Resource.Error("Could not find weather for '$cityToFetch'. Check the city name.")
                }
            } catch (e: Exception) {
                _weatherState.value = Resource.Error(e.message ?: "Could not fetch weather for '$cityToFetch'.")
            }
        }
    }

    fun formatTemperature(tempInCelsius: Double): String {
        return if (tempUnit == "Fahrenheit") {
            val f = tempInCelsius * 9.0 / 5.0 + 32.0
            String.format(Locale.getDefault(), "%.1f°F", f)
        } else {
            String.format(Locale.getDefault(), "%.1f°C", tempInCelsius)
        }
    }
}
