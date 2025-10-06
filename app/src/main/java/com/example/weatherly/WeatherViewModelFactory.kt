package com.example.weatherly.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherly.BuildConfig
import com.example.weatherly.data.api.RetrofitInstance
import com.example.weatherly.data.db.WeatherDatabase
import com.example.weatherly.data.repository.WeatherRepository


class WeatherViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            // Manually create the dependencies needed for the ViewModel
            val dao = WeatherDatabase.getDatabase(application).weatherDao()
            val repository = WeatherRepository(RetrofitInstance.api, dao)
            val apiKey = BuildConfig.OPENWEATHER_API_KEY

            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository, apiKey) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}