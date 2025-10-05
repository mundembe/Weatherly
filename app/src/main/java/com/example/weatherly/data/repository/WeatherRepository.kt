package com.example.weatherly.data.repository

import com.example.weatherly.data.api.WeatherApi
import com.example.weatherly.data.db.WeatherDao
import com.example.weatherly.data.db.WeatherEntity

class WeatherRepository(private val api: WeatherApi, private val dao: WeatherDao) {

    suspend fun getWeather(city: String, apiKey: String): WeatherEntity {
        return try {
            val response = api.getWeather(city, apiKey)
            val entity = WeatherEntity(
                city = response.name,
                temperature = response.main.temp,
                description = response.weather.firstOrNull()?.description ?: "Unknown",
                icon = response.weather.firstOrNull()?.icon ?: "01d"
            )
            dao.insertWeather(entity)
            entity
        } catch (e: Exception) {
            dao.getWeather(city) ?: throw e
        }
    }
}
