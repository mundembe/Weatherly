package com.example.weatherly

data class WeatherResponse(
    val main: Main,
    val weather: List<WeatherDto>,
    val name: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val humidity: Int
)

data class WeatherDto(
    val description: String,
    val icon: String // e.g., "10d"
)
