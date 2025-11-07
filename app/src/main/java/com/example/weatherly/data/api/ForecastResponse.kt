package com.example.weatherly.data.api

import com.google.gson.annotations.SerializedName

// Main response object
data class ForecastResponse(
    @SerializedName("list") val list: List<ForecastItem>,
    @SerializedName("city") val city: City
)

data class ForecastItem(
    @SerializedName("dt_txt") val dateTime: String,
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<WeatherInfo>
)

data class City(
    @SerializedName("name") val name: String
)