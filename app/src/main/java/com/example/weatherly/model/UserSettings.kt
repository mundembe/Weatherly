package com.example.weatherly.model

data class UserSettings(
    val language: String = "en",
    val temperatureUnit: String = "Celsius",
    val notificationsEnabled: Boolean = true
)
