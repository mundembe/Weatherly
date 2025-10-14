package com.example.weatherly.data.api

import com.google.gson.annotations.SerializedName

// Main response object
data class ForecastResponse(
    @SerializedName("hourly") val hourly: List<HourlyForecast>,
    @SerializedName("daily") val daily: List<DailyForecast>
)

// Data class for each hourly forecast item
data class HourlyForecast(
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("temp") val temp: Double,
    @SerializedName("weather") val weather: List<WeatherInfo>
)

// Data class for each daily forecast item
data class DailyForecast(
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("temp") val temp: Temp,
    @SerializedName("weather") val weather: List<WeatherInfo>
)

// Nested temperature object for daily forecast
data class Temp(
    @SerializedName("day") val day: Double,
    @SerializedName("min") val min: Double,
    @SerializedName("max") val max: Double
)

// Re-used from your existing API structure
// data class WeatherInfo(
//    val icon: String,
//    val description: String
// )
