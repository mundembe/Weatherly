package com.example.weatherly.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherEntity(
    @PrimaryKey val city: String,
    val temperature: Double,
    val description: String,
    val icon: String,
    val timestamp: Long = System.currentTimeMillis()
)