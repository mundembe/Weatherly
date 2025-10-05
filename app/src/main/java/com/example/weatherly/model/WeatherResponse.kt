package com.example.weatherly.model

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>
)

data class Main(val temp: Double, val feels_like: Double)
data class Weather(val description: String, val icon: String)
