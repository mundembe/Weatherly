package com.example.weatherly.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherly.data.model.WeatherData
// import com.example.weatherly.data.db.WeatherEntity // No longer directly used in state
import com.example.weatherly.data.repository.SettingsRepository
import com.example.weatherly.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import androidx.compose.runtime.*
import com.example.weatherly.data.api.ForecastItem
import com.example.weatherly.data.repository.WeatherRepository
import java.util.*

// --- ENUM ---
// Handles forecast display type toggle (hourly/daily)
enum class ForecastType {
    HOURLY, DAILY
}

// --- VIEWMODEL ---
class WeatherViewModel(
    application: Application,
    private val repo: WeatherRepository,
    private val apiKey: String
) : AndroidViewModel(application) {

    // Holds combined current + forecast weather
    private val _weatherState = MutableStateFlow<Resource<WeatherData>>(Resource.Loading)
    val weatherState = _weatherState.asStateFlow()

    // Forecast type (HOURLY or DAILY)
    private val _forecastType = mutableStateOf(ForecastType.HOURLY)
    val forecastType: State<ForecastType> = _forecastType

    // City search query
    private val _cityQuery = mutableStateOf("Cape Town") // Default
    val cityQuery: State<String> = _cityQuery

    // User preferences (temperature unit, etc.)
    var tempUnit by mutableStateOf("Celsius")
        private set
    private val settingsRepo = SettingsRepository(application)

    init {
        // Load temperature unit from DataStore when ViewModel starts
        viewModelScope.launch {
            settingsRepo.userSettings.collect { settings ->
                tempUnit = settings.temperatureUnit
            }
        }
    }

    /**
     * Updates the current city query (from search bar)
     */
    fun updateCityQuery(newQuery: String) {
        _cityQuery.value = newQuery
    }

    /**
     * Switch between hourly and daily forecasts
     */
    fun setForecastType(type: ForecastType) {
        _forecastType.value = type
    }

    /**
     * Fetches current weather + forecast for the given city.
     * Merged to use WeatherRepository.getWeatherAndForecast().
     */
    fun fetchWeather() {
        val cityToFetch = _cityQuery.value.trim()
        if (cityToFetch.isBlank()) {
            _weatherState.value = Resource.Error("City name cannot be empty.")
            return
        }

        viewModelScope.launch {
            _weatherState.value = Resource.Loading
            try {
                val result: WeatherData? = repo.getWeatherAndForecast(cityToFetch, apiKey)
                if (result != null) {
                    _weatherState.value = Resource.Success(result)
                } else {
                    _weatherState.value = Resource.Error("Could not find weather for '$cityToFetch'.")
                }
            } catch (e: Exception) {
                _weatherState.value =
                    Resource.Error(e.message ?: "Could not fetch weather for '$cityToFetch'.")
            }
        }
    }

    /**
     * Converts Celsius to Fahrenheit if user preference is set.
     */
    fun formatTemperature(tempInCelsius: Double): String {
        return if (tempUnit == "Fahrenheit") {
            val f = tempInCelsius * 9.0 / 5.0 + 32.0
            String.format(Locale.getDefault(), "%.1f°F", f)
        } else {
            String.format(Locale.getDefault(), "%.1f°C", tempInCelsius)
        }
    }

    /**
     * Returns a filtered list of forecast data depending on the selected forecast type.
     * The WeatherResponse API returns 3-hourly intervals for 5 days.
     * This method groups those into daily averages if the user chooses DAILY.
     */
    fun getFilteredForecast(allItems: List<ForecastItem>): List<ForecastItem> {
        return when (_forecastType.value) {
            ForecastType.HOURLY -> {
                // Show next 12 (≈36 hours) 3-hour readings
                allItems.take(12)
            }
            ForecastType.DAILY -> {
                // Group by date (yyyy-MM-dd) and pick the first noon entry per day
                allItems
                    .groupBy { it.dateTime.substring(0, 10) } // group by date portion
                    .mapNotNull { (_, dayList) ->
                        // Find the one closest to 12:00:00 or fallback to mid element
                        dayList.find { it.dateTime.contains("12:00:00") } ?: dayList[dayList.size / 2]
                    }
                    .take(5) // limit to 5 days
            }
        }
    }

}