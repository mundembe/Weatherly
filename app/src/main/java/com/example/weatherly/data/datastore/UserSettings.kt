package com.example.weatherly.data.datastore

data class UserSettings(
    val language: String = "en",
    val temperatureUnit: String = "Celsius",
    val notificationsEnabled: Boolean = true
)