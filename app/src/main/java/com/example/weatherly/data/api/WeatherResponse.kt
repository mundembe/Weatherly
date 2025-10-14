package com.example.weatherly.data.api

import com.google.gson.annotations.SerializedName

// This is your existing file, we are adding the 'Coord' property and data class
data class WeatherResponse(
    @SerializedName("weather") val weather: List<WeatherInfo>,
    @SerializedName("main") val main: Main,
    @SerializedName("name") val name: String,

    // --- FIX: Add the 'coord' property to capture latitude and longitude ---
    @SerializedName("coord") val coord: Coord
)

data class WeatherInfo(
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class Main(
    @SerializedName("temp") val temp: Double
)

// --- FIX: Add the new data class to hold coordinate data ---
data class Coord(
    @SerializedName("lon") val lon: Double,
    @SerializedName("lat") val lat: Double
)
