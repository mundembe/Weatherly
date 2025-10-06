package com.example.weatherly.ui.home

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.weatherly.BuildConfig
import com.example.weatherly.WeatherScreen
import com.example.weatherly.data.api.RetrofitInstance
import com.example.weatherly.data.db.WeatherDatabase
import com.example.weatherly.data.repository.WeatherRepository
import com.example.weatherly.viewmodel.WeatherViewModel
import com.example.weatherly.viewmodel.WeatherViewModelFactory

@Composable
fun HomeScreenWrapper(city: String = "London") {
    // to correctly create singletons like the database.
    val application = LocalContext.current.applicationContext as Application

    // FIX: Use a factory that accepts the application context.
    // The factory is now responsible for creating the repository and its dependencies.
    val weatherViewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory(application)
    )

    // This part remains the same. It triggers the weather fetch when the city changes.
    LaunchedEffect(city) {
        weatherViewModel.fetchWeather(city)
    }

    // FIX: Add the `onRetry` lambda, which is required by WeatherScreen.
    // This allows the user to try fetching the weather again if it fails.
    WeatherScreen(
        viewModel = weatherViewModel,
        onRetry = { weatherViewModel.fetchWeather(city) }
    )
}
