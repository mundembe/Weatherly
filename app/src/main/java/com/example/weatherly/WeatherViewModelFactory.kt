package com.example.weatherly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherly.data.repository.WeatherRepository

class WeatherViewModelFactory(
    private val repo: WeatherRepository,
    private val apiKey: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repo, apiKey) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}