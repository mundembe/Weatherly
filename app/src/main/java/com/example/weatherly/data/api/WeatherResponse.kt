package com.example.weatherly.data.api

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("weather") val weather: List<WeatherInfo>,
    @SerializedName("main") val main: Main,
    @SerializedName("name") val name: String,
)

data class WeatherInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class Main(
    @SerializedName("temp") val temp: Double
)

