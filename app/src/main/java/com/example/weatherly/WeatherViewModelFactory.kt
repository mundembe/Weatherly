package com.example.weatherly

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherly.data.api.RetrofitInstance
import com.example.weatherly.data.db.WeatherDatabase
import com.example.weatherly.data.repository.WeatherRepository
import com.example.weatherly.viewmodel.WeatherViewModel


class WeatherViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            // Manually create the dependencies needed for the ViewModel
            val dao = WeatherDatabase.getDatabase(application).weatherDao()
            val repository = WeatherRepository(RetrofitInstance.api, dao)
            val apiKey = BuildConfig.OPENWEATHER_API_KEY

            @Suppress("UNCHECKED_CAST")
            // FIX: Pass the 'application' instance to the ViewModel's constructor
            return WeatherViewModel(application, repository, apiKey) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
