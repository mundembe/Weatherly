
package com.example.weatherly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherly.data.db.WeatherEntity
import com.example.weatherly.data.repository.WeatherRepository
import com.example.weatherly.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repo: WeatherRepository,
    private val apiKey: String
) : ViewModel() {

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