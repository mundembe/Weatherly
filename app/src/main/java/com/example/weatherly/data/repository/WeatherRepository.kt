package com.example.weatherly.data.repository

import com.example.weatherly.data.api.ForecastResponse
import com.example.weatherly.data.api.WeatherApi
import com.example.weatherly.data.db.WeatherDao
import com.example.weatherly.data.db.WeatherEntity

// --- NEW: Data class to hold both current weather and forecast ---
data class WeatherData(
    val currentWeather: WeatherEntity,
    val forecast: ForecastResponse?
)

class WeatherRepository(private val api: WeatherApi, private val dao: WeatherDao) {

    // --- REVISED: This function will now get both current weather and the forecast ---
    suspend fun getWeatherAndForecast(city: String, apiKey: String): WeatherData? {
        return try {
            // Step 1: Get current weather to obtain city name, coordinates, and other info
            val currentResponse = api.getWeather(city, apiKey)
            val entity = WeatherEntity(
                city = currentResponse.name,
                temperature = currentResponse.main.temp,
                description = currentResponse.weather.firstOrNull()?.description ?: "Unknown",
                icon = currentResponse.weather.firstOrNull()?.icon ?: "01d"
            )
            dao.insertWeather(entity)

            // Step 2: Use coordinates from the first call to get the forecast
            val forecastResponse = api.getForecast(
                lat = currentResponse.coord.lat,
                lon = currentResponse.coord.lon,
                apiKey = apiKey
            )

            WeatherData(currentWeather = entity, forecast = forecastResponse)
        } catch (e: Exception) {
            // In case of network error, try to fetch at least the current weather from cache
            val cachedWeather = dao.getWeather(city)
            cachedWeather?.let {
                WeatherData(currentWeather = it, forecast = null) // No forecast data offline
            }
        }
    }
}
