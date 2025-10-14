package com.example.weatherly.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
// import com.example.weatherly.data.db.WeatherEntity // No longer directly used in state
import com.example.weatherly.data.repository.SettingsRepository
import com.example.weatherly.data.repository.WeatherData // Correctly imported
import com.example.weatherly.data.repository.WeatherRepository
import com.example.weatherly.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

// Enum to manage forecast type
enum class ForecastType {
    HOURLY, DAILY
}

class WeatherViewModel(
    application: Application,
    private val repo: WeatherRepository,
    private val apiKey: String
) : AndroidViewModel(application) {

    // --- FIX: Change the state type from WeatherEntity to WeatherData ---
    private val _weatherState = MutableStateFlow<Resource<WeatherData>>(Resource.Loading)
    val weatherState = _weatherState.asStateFlow()

    // --- NEW: State to manage the selected forecast type ---
    private val _forecastType = mutableStateOf(ForecastType.HOURLY)
    val forecastType: State<ForecastType> = _forecastType

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

    // --- NEW: Function to change the forecast type ---
    fun setForecastType(type: ForecastType) {
        _forecastType.value = type
    }

    // REVISED: fetchWeather now gets the combined data
    fun fetchWeather() {
        val cityToFetch = _cityQuery.value.trim()
        if (cityToFetch.isBlank()) {
            // Use the correct state type for the error
            _weatherState.value = Resource.Error("City name cannot be empty.")
            return
        }

        viewModelScope.launch {
            _weatherState.value = Resource.Loading
            try {
                val result: WeatherData? = repo.getWeatherAndForecast(cityToFetch, apiKey)
                if (result != null) {
                    // This now correctly matches the _weatherState type
                    _weatherState.value = Resource.Success(result)
                } else {
                    _weatherState.value = Resource.Error("Could not find weather for '$cityToFetch'.")
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
