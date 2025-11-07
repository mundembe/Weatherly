package com.example.weatherly.data.model

import com.example.weatherly.data.api.ForecastResponse
import com.example.weatherly.data.api.WeatherResponse

data class WeatherData(
    val current: WeatherResponse,
    val forecast: ForecastResponse
)